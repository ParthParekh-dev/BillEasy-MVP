package com.parth.billeasy.view

import com.parth.billeasy.model.VideoList

interface IMainView {
    fun showLoading()
    fun hideLoading()
    fun dataResult(videoList:ArrayList<VideoList>)
}