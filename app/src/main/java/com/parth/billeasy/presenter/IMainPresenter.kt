package com.parth.billeasy.presenter

import android.content.Context
import com.parth.billeasy.model.MovieList

interface IMainPresenter {
    fun getDataFromServer(int: Int)
    fun isOnline(context: Context): Boolean
    fun getDataFromStorage()
}