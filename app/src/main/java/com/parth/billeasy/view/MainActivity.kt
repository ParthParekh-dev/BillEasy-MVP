package com.parth.billeasy.view

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.parth.billeasy.R
import com.parth.billeasy.adapter.MovieAdapter
import com.parth.billeasy.model.VideoList
import com.parth.billeasy.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IMainView {

    private lateinit var materialDialog: MaterialDialog
    private lateinit var mainPresenter: MainPresenter
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var searchView: SearchView

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mainPresenter = MainPresenter(this)

        val layoutManager =
            LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        movieAdapter = MovieAdapter(baseContext)
        recyclerView.adapter = movieAdapter

        mainPresenter.getDataFromServer()
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

    override fun dataResult(videoList: ArrayList<VideoList>) {
        runOnUiThread {
            movieAdapter.setDataValue(videoList)
            movieAdapter.notifyDataSetChanged()
        }
    }
}
