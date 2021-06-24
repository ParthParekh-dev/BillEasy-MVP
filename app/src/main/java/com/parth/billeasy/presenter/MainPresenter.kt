package com.parth.billeasy.presenter

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.JsonParser
import com.parth.billeasy.model.MovieList
import com.parth.billeasy.view.IMainView
import okhttp3.*
import okio.IOException
import org.json.JSONException
import org.json.JSONObject


class MainPresenter(private val view: IMainView) : IMainPresenter {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getDataFromServer(page: Int) {

        view.showLoading()

        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/now_playing?api_key=b5526107bcceb481e5fac4760354a63c&language=en-US&page="+page.toString())
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


                val videoListArray = arrayListOf<MovieList>()
                try {
                    val obj = JSONObject(json.toString())
                    val userArray = obj.getJSONArray("results")
                    for (i in 0 until userArray.length()) {
                        val userDetail = userArray.getJSONObject(i)
                        var video  = MovieList(
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun isOnline(context: Context):Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    override fun getDataFromStorage() {
            view.storageResult()
    }
}