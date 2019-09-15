package com.scb.mobilephone.presenters.interfaces

import android.content.Context
import android.content.Intent
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity

interface ListInterface {

    interface ListView {

        fun showLoading()

        fun hideLoading()

        fun showPhonesList(phonesSortedList: List<PhonesListEntity>?)
    }

    interface ListPresenter {

        fun postTask(task: Runnable)

        fun keepInDatabase(phonesList: List<PhonesListEntity>)

        fun feedPhonesList(context: Context)

        fun getSortType(sortType: String)

        fun getPhones(): List<PhonesListEntity>

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

        fun addFavoriteItem(phone: PhonesListEntity)

        fun removeFavoriteItem(id: Int)
    }
}