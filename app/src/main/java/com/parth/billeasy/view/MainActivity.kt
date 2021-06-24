package com.parth.billeasy.view

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.Menu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import com.parth.billeasy.R
import com.parth.billeasy.adapter.MovieAdapter
import com.parth.billeasy.model.MovieList
import com.parth.billeasy.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), IMainView {

    private lateinit var materialDialog: MaterialDialog
    private lateinit var mainPresenter: MainPresenter
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var searchView: SearchView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val page = 1

        mainPresenter = MainPresenter(this)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val layoutManager =
            LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        movieAdapter = MovieAdapter(baseContext)
        recyclerView.adapter = movieAdapter

//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    page=page+1
//                    mainPresenter.getDataFromServer(page)
//                    Toast.makeText(applicationContext,"Loading Page "+page,Toast.LENGTH_LONG).show()
//                }
//            }
//        })
        if(mainPresenter.isOnline(applicationContext)){
            mainPresenter.getDataFromServer(page)
        }
        else{
            mainPresenter.getDataFromStorage()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu!!.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.setQueryHint("Search Title")

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    movieAdapter.getFilter().filter("")
                } else {
                    movieAdapter.getFilter().filter(newText)
                }
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun showLoading() {

        materialDialog = MaterialDialog.Builder(this)
            .title("Loading...")
            .content("Please Wait")
            .progress(true, 0)
            .show()

    }

    override fun hideLoading() {
        materialDialog.dismiss()
    }

    override fun dataResult(movieList: ArrayList<MovieList>) {

        editor = sharedPreferences.edit()

        val gson = Gson()
        val jsonText = gson.toJson(movieList)
        editor.putString("movies", jsonText)
        editor.apply()

        runOnUiThread {
            movieAdapter.setDataValue(movieList)
            movieAdapter.notifyDataSetChanged()
        }
    }

    @ExperimentalStdlibApi
    override fun storageResult() {

        val gson = Gson()
        val jsonText: String? = sharedPreferences.getString("movies", null)
        val movieList = gson.fromJson(
            jsonText,
            Array<MovieList>::class.java
        )

        runOnUiThread {
            movieAdapter.setDataValue(ArrayList(Arrays.asList(*movieList)))
            movieAdapter.notifyDataSetChanged()
        }
    }


}
