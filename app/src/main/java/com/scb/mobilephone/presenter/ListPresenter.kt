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
import com.scb.mobilephone.network.ApiInterface
import com.scb.mobilephone.view.ListFragment.Companion.mAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPresenter(_view: ListInterface.ListView) : ListInterface.ListPresenter {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var mDataArray: ArrayList<PhoneBean> = ArrayList()
        var mDataSortedArray: ArrayList<PhoneBean> = ArrayList()
        var favoriteItem: ArrayList<PhoneBean> = ArrayList()
    }

    private var view: ListInterface.ListView = _view

    override fun feedPhonesList() {
        val _call = ApiInterface.getClient().getPhones()

        _call.enqueue(object : Callback<List<PhoneBean>> {
            override fun onFailure(call: Call<List<PhoneBean>>, t: Throwable) {
                Log.d("error", t.message.toString())
            }

            override fun onResponse(call: Call<List<PhoneBean>>, response: Response<List<PhoneBean>>) {
                Log.d("response", response.body().toString())
                if (response.isSuccessful) {
                    mDataArray.clear()
                    mDataArray.addAll((response.body()!!))
                    view.showPhonesList(mDataArray)
                    view.hideLoading()
                }
            }

        })
    }

    override fun sortPhonesList(phonesList: ArrayList<PhoneBean>, sort: String) {
        mDataSortedArray.clear()
        when (sort) {
            PriceLH -> {
                mDataSortedArray.addAll(phonesList.sortedBy { it.price })
                mDataArray.clear()
                mDataArray.addAll(mDataSortedArray)
                Log.d("sorted", mDataArray.toString())
            }
            PriceHL -> {
                mDataSortedArray.addAll(phonesList.sortedByDescending { it.price })
                mDataArray.clear()
                mDataArray.addAll(mDataSortedArray)
                Log.d("sorted", mDataArray.toString())
            }
            RatingHL -> {
                mDataSortedArray.addAll(phonesList.sortedByDescending { it.rating })
                mDataArray.clear()
                mDataArray.addAll(mDataSortedArray)
                Log.d("sorted", mDataArray.toString())
            }
            else -> {
                mDataSortedArray.addAll(phonesList)
                mDataArray.clear()
                mDataArray.addAll(mDataSortedArray)
                Log.d("sorted", mDataArray.toString())
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun getFavoriteItems(context: Context) {
        LocalBroadcastManager.getInstance(context!!).registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    favoriteItem.clear()
                    favoriteItem.addAll(intent.getParcelableArrayListExtra(GetFavoriteItems))
                }
            },
            IntentFilter(FavoriteItemsFromFavoriteToList)
        )
    }

    override fun sendFavoriteItems(context: Context, content: ArrayList<PhoneBean>) {
        // send favorite items from list page to fragment page
        Intent(FavoriteItemsFromListToFavorite).let {
            it.putExtra(RecieveFavoriteItems, content)
            LocalBroadcastManager.getInstance(context!!).sendBroadcast(it)
        }
    }
}