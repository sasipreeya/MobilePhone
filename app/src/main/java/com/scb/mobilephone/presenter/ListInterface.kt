package com.scb.mobilephone.presenter

import android.content.Context
import com.scb.mobilephone.models.PhoneBean

interface ListInterface {

    interface ListView {

        fun showLoading()

        fun hideLoading()

        fun showPhonesList(phonesSortedList: ArrayList<PhoneBean>)
    }

    interface ListPresenter {

        fun feedPhonesList()

        fun sortPhonesList(phonesList: ArrayList<PhoneBean>, sort: String)

        fun getFavoriteItems(context: Context)

        fun sendFavoriteItems(context: Context, content: ArrayList<PhoneBean>)
    }
}