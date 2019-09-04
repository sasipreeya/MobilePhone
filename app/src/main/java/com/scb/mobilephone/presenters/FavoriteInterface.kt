package com.scb.mobilephone.presenters

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
    }

}