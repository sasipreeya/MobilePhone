package com.scb.mobilephone.presenters

interface DetailInterface {

    interface DetailView {

        fun showImageDetail(urlList: ArrayList<String>)
    }

    interface DetailPresenter {

        fun feedDatailData(id: Int)
    }

}