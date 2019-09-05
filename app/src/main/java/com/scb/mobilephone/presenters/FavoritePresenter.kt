package com.scb.mobilephone.presenters

import android.content.Context
import android.util.Log
import com.scb.mobilephone.extensions.ThreadManager
import com.scb.mobilephone.models.database.AppDatabase
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.presenters.interfaces.FavoriteInterface

class FavoritePresenter(_view: FavoriteInterface.FavoriteView) :
    FavoriteInterface.FavoritePresenter {

    private var favoritesList: ArrayList<FavoritesEntity> = ArrayList()
    private var mDatabase: AppDatabase? = null
    private lateinit var mThreadManager: ThreadManager

    private var view: FavoriteInterface.FavoriteView = _view

    override fun setupDatabase(context: Context) {
        mDatabase = AppDatabase.getInstance(context).also {
            it.openHelper.readableDatabase
        }
    }

    override fun setupTreadManager() {
        mThreadManager = ThreadManager("database3").also {
            it.start()
        }
    }

    override fun getFavoritesList(context: Context) {
        val task = Runnable {
            val favoritesList = mDatabase!!.favoritesListDao().queryFavoritesList()!!
            this.favoritesList.clear()
            this.favoritesList.addAll(favoritesList)
        }
        mThreadManager.postTask(task)
        view.showFavoritesList(favoritesList)
        view.hideLoading()
    }

    override fun removeFavoriteItem(id: Int) {
        val task = Runnable {
            mDatabase!!.favoritesListDao()
                .deleteFavorite(id)
            val list = mDatabase!!.favoritesListDao().queryFavoritesList()
            Log.d("database", list.toString())
            Log.d("database", "${list?.size}")
        }
        mThreadManager.postTask(task)
    }
}