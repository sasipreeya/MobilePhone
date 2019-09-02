package com.scb.mobilephone.presenter

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.scb.mobilephone.extensions.*
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.presenter.ListPresenter.Companion.favoriteItem
import com.scb.mobilephone.view.FavoriteFragment

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
                    // favoriteItem.clear()
                    // favoriteItem.addAll(intent.getParcelableArrayListExtra(RecieveFavoriteItems))
                    favoritesSortList.clear()
                    favoritesSortList.addAll(favoriteItem)
                    view.showFavoritesList(favoritesSortList)
                    view.hideLoading()
                }
            },
            IntentFilter(FavoriteItemsFromListToFavorite)
        )
    }

    override fun sortFavoritesList(phonesList: ArrayList<PhoneBean>, sort: String) {
        favoritesSortList.clear()
        when (sort) {
            PriceLH -> {
                favoritesSortList.addAll(phonesList.sortedBy { it.price })
                favoriteItem.clear()
                favoriteItem.addAll(favoritesSortList)
                Log.d("sorted", favoriteItem.toString())
            }
            PriceHL -> {
                favoritesSortList.addAll(phonesList.sortedByDescending { it.price })
                favoriteItem.clear()
                favoriteItem.addAll(favoritesSortList)
                Log.d("sorted", favoriteItem.toString())
            }
            RatingHL -> {
                favoritesSortList.addAll(phonesList.sortedByDescending{ it.rating })
                favoriteItem.clear()
                favoriteItem.addAll(favoritesSortList)
                Log.d("sorted", favoriteItem.toString())
            }
            else -> {
                favoritesSortList.addAll(phonesList)
                favoriteItem.clear()
                favoriteItem.addAll(favoritesSortList)
            }
        }
        FavoriteFragment.mAdapter.notifyDataSetChanged()
    }

    override fun sendFavoriteItems(context: Context, content: ArrayList<PhoneBean>) {
        // send favorite item after deleted back to list page
        Intent(FavoriteItemsFromFavoriteToList).let {
            it.putExtra(GetFavoriteItems, content)
            LocalBroadcastManager.getInstance(context).sendBroadcast(it)
        }
    }

}