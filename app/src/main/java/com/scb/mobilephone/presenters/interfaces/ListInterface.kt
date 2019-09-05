package com.scb.mobilephone.presenters.interfaces

import android.content.Context
import android.content.Intent
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.models.database.entities.FavoritesEntity

interface ListInterface {

    interface ListView {

        fun showLoading()

        fun hideLoading()

        fun showPhonesList(phonesSortedList: ArrayList<PhoneBean>)
    }

    interface ListPresenter {

        fun postTask(task: Runnable)

        fun keepInDatabase(phonesList: ArrayList<PhoneBean>)

        fun feedPhonesList(context: Context)

        fun getPhones(): ArrayList<PhoneBean>

        fun getFavorites(): List<FavoritesEntity>

        fun getPhonesList()

        fun openDetailPage(
            intent: Intent,
            thumbImageURL: String,
            name: String,
            brand: String,
            description: String,
            id: Int,
            rating: Double,
            price: Double
        )

        fun setupDatabase(context: Context)

        fun setupTreadManager()

        fun addFavoriteItem(phone: PhoneBean)

        fun removeFavoriteItem(id: Int)
    }
}