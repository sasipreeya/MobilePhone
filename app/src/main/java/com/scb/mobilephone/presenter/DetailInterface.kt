package com.scb.mobilephone.presenter

interface DetailInterface {

    interface DetailView {

        fun showImageDetail(urlList: ArrayList<String>)
    }

    interface DetailPresenter {

        fun feedDatailData(id: Int)
    }

}