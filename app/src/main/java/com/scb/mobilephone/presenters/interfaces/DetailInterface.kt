package com.scb.mobilephone.presenters.interfaces

interface DetailInterface {

    interface DetailView {

        fun showImageDetail(urlList: ArrayList<String>)
    }

    interface DetailPresenter {

        fun feedDatailData(id: Int)
    }

}