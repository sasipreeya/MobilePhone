package com.scb.mobilephone.presenters.interfaces

import android.content.Context
import com.scb.mobilephone.models.database.entities.FavoritesEntity

interface FavoriteInterface {

    interface FavoriteView {

        fun showLoading()

        fun hideLoading()

        fun showFavoritesList(phonesSortedList: ArrayList<FavoritesEntity>)
    }

    interface FavoritePresenter {

        fun setupDatabase(context: Context)

        fun setupTreadManager()

        fun getFavoritesList(context: Context)

        fun removeFavoriteItem(id: Int)
    }

}