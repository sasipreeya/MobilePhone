package com.scb.mobilephone.presenter

import android.content.Context
import com.scb.mobilephone.models.PhoneBean

interface FavoriteInterface {

    interface FavoriteView {

        fun showLoading()

        fun hideLoading()

        fun showFavoritesList(phonesSortedList: ArrayList<PhoneBean>)
    }

    interface FavoritePresenter {

        fun getFavoritesList(context: Context)

        fun sendFavoriteItems(context: Context, content: ArrayList<PhoneBean>)
    }

}