package com.scb.mobilephone.presenters.interfaces

import android.content.Context
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity

interface MainInterface {

    interface MainPresenter {

        fun sortList(phonesList: List<PhonesListEntity>, favoriteslist: List<FavoritesEntity>, sort: String)

        fun postTask(task: Runnable)

        fun getPhones(): List<PhonesListEntity>

        fun getFavorites(): List<FavoritesEntity>

        fun setupDatabase(context: Context)

        fun setupTreadManager()

        fun updatePhonesList(sortedList: List<PhonesListEntity>)

        fun updateFavoritesList(sortedList: List<FavoritesEntity>)
    }
}