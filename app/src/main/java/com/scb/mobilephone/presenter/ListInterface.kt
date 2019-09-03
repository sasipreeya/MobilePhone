package com.scb.mobilephone.presenter

import android.content.Context
import android.content.Intent
import com.scb.mobilephone.models.PhoneBean

interface ListInterface {

    interface ListView {

        fun showLoading()

        fun hideLoading()

        fun showPhonesList(phonesSortedList: ArrayList<PhoneBean>)
    }

    interface ListPresenter {

        fun feedPhonesList()

        fun getPhonesList()

        fun getFavoriteItems(context: Context)

        fun sendFavoriteItems(context: Context, content: ArrayList<PhoneBean>)

        fun openDetailPage(intent: Intent, thumbImageURL: String, name: String, brand: String, description: String, id: Int, rating: Double, price: Double)
    }
}