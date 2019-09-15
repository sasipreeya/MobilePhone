package com.scb.mobilephone.presenters.interfaces

import android.content.Context
import android.content.Intent
import com.scb.mobilephone.models.database.entities.FavoritesEntity

interface FavoriteInterface {

    interface FavoriteView {

        fun showLoading()

        fun hideLoading()

        fun showFavoritesList(favoritesSortedList: ArrayList<FavoritesEntity>)
    }

    interface FavoritePresenter {

        fun setupDatabase(context: Context)

        fun setupTreadManager()

        fun postTask(task: Runnable)

        fun getSortType(sortType: String)

        fun getFavoritesList(context: Context)

        fun removeFavoriteItem(id: Int)

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
    }

}