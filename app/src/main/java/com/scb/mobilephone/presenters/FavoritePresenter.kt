package com.scb.mobilephone.presenters

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.scb.mobilephone.extensions.FavoriteItemsFromFavoriteToList
import com.scb.mobilephone.extensions.FavoriteItemsFromListToFavorite
import com.scb.mobilephone.extensions.GetFavoriteItems
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.presenters.ListPresenter.Companion.favoriteItem
import com.scb.mobilephone.presenters.ListPresenter.Companion.mDatabaseAdapter

class FavoritePresenter(_view: FavoriteInterface.FavoriteView) : FavoriteInterface.FavoritePresenter {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var favoritesSortList: ArrayList<PhoneBean> = ArrayList()
    }

    private var view: FavoriteInterface.FavoriteView = _view

    override fun getFavoritesList(context: Context) {
        val task = Runnable {
            val favoritesList = mDatabaseAdapter!!.favoritesListDao().queryFavoritesList()!!.favoritesList
            favoritesSortList.clear()
            favoritesSortList.addAll(favoritesList)
        }
        ListPresenter.mThreadManager.postTask(task)
        view.showFavoritesList(favoritesSortList)
        view.hideLoading()
    }

}