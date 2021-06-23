package com.parth.billeasy.presenter

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.JsonParser
import com.parth.billeasy.model.VideoList
import com.parth.billeasy.view.IMainView
import okhttp3.*
import okio.IOException
import org.json.JSONException
import org.json.JSONObject


class MainPresenter(private val view: IMainView) : IMainPresenter {


    @RequiresApi(Build.VERSION_CODES.N)
    override fun getDataFromServer() {

        view.showLoading()
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/now_playing?api_key=b5526107bcceb481e5fac4760354a63c&language=en-US&page=1")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                view.hideLoading()
            }
            override fun onResponse(call: Call, response: Response) {
                view.hideLoading()
                val result = response.body?.string()
                val parser = JsonParser()
                val json = parser.parse(result)


                val videoListArray = arrayListOf<VideoList>()
                try {
                    val obj = JSONObject(json.toString())
                    val userArray = obj.getJSONArray("results")
                    for (i in 0 until userArray.length()) {
                        val userDetail = userArray.getJSONObject(i)
                        var video  = VideoList(
                            userDetail.getString("id"),
                            userDetail.getString("original_title"),
                            "4",
                            userDetail.getString("vote_average"),
                            "https://homepages.cae.wisc.edu/~ece533/images/airplane.png",
                            userDetail.getString("release_date"))
                        videoListArray.add(video)
                    }
                    view.dataResult(videoListArray)
                }
                catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }
}