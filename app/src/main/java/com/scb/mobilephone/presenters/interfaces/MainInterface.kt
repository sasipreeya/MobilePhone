package com.scb.mobilephone.presenters.interfaces

import android.content.Context
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.models.database.entities.FavoritesEntity

interface MainInterface {

    interface MainPresenter {

        fun sortPhonesList(list: ArrayList<PhoneBean>, sort: String)

        fun sortFavoritesList(list: List<FavoritesEntity>, sort: String)

        fun postTask(task: Runnable)

        fun getPhones(): ArrayList<PhoneBean>

        fun getFavorites(): List<FavoritesEntity>

        fun setupDatabase(context: Context)

        fun setupTreadManager()

        fun updatePhonesList(sortedList: ArrayList<PhoneBean>)

        fun updateFavoritesList(sortedList: List<FavoritesEntity>)
    }
}