package com.scb.mobilephone.presenter

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
import com.scb.mobilephone.presenter.ListPresenter.Companion.favoriteItem

class FavoritePresenter(_view: FavoriteInterface.FavoriteView) : FavoriteInterface.FavoritePresenter {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var favoritesSortList: ArrayList<PhoneBean> = ArrayList()
    }

    private var view: FavoriteInterface.FavoriteView = _view

    override fun getFavoritesList(context: Context) {
        // get favorite items from list page
        LocalBroadcastManager.getInstance(context).registerReceiver(
            object : BroadcastReceiver(){
                override fun onReceive(context: Context, intent: Intent) {
                    favoritesSortList.clear()
                    favoritesSortList.addAll(favoriteItem)
                    // favoritesSortList.addAll(intent.getParcelableArrayListExtra(RecieveFavoriteItems))
                    view.showFavoritesList(favoritesSortList)
                    view.hideLoading()
                }
            },
            IntentFilter(FavoriteItemsFromListToFavorite)
        )
    }

    override fun sendFavoriteItems(context: Context, content: ArrayList<PhoneBean>) {
        // send favorite item after deleted back to list page
        Intent(FavoriteItemsFromFavoriteToList).let {
            it.putExtra(GetFavoriteItems, content)
            LocalBroadcastManager.getInstance(context).sendBroadcast(it)
        }
    }

}