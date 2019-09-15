package com.scb.mobilephone.presenters

import android.content.Context
import android.content.Intent
import com.scb.mobilephone.extensions.ThreadManager
import com.scb.mobilephone.models.database.AppDatabase
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.presenters.interfaces.FavoriteInterface

class FavoritePresenter(_view: FavoriteInterface.FavoriteView, private val sortPresenter: SortInterface.SortPresenter) :
    FavoriteInterface.FavoritePresenter {

    private var favoritesList: ArrayList<FavoritesEntity> = ArrayList()
    private var mDatabase: AppDatabase? = null
    private lateinit var mThreadManager: ThreadManager
    private var mSortType = "none"
    private var view: FavoriteInterface.FavoriteView = _view

    override fun getSortType(sortType: String) {
        this.mSortType = sortType
        sortPresenter.sortFavoritesList(mSortType, favoritesList.toList())
    }

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

    override fun postTask(task: Runnable) {
        mThreadManager.postTask(task)
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
        }
        mThreadManager.postTask(task)
    }

    override fun openDetailPage(
        intent: Intent,
        thumbImageURL: String,
        name: String,
        brand: String,
        description: String,
        id: Int,
        rating: Double,
        price: Double
    ) {
        intent.putExtra("image", thumbImageURL)
        intent.putExtra("name", name)
        intent.putExtra("brand", brand)
        intent.putExtra("detail", description)
        intent.putExtra("id", id)
        intent.putExtra("rating", rating)
        intent.putExtra("price", price)
    }
}