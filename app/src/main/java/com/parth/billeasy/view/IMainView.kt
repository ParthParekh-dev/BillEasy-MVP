package com.parth.billeasy.view

import com.parth.billeasy.model.MovieList

interface IMainView {
    fun showLoading()
    fun hideLoading()
    fun dataResult(movieList:ArrayList<MovieList>)
    fun storageResult()
}