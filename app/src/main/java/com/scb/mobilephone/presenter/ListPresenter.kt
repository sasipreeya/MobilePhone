package com.scb.mobilephone.presenter

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.scb.mobilephone.extensions.*
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.models.database.AppDatabase
import com.scb.mobilephone.models.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPresenter(_view: ListInterface.ListView) : ListInterface.ListPresenter {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var mDataArray: ArrayList<PhoneBean> = ArrayList()
        var favoriteItem: ArrayList<PhoneBean> = ArrayList()
    }

    var mDatabaseAdapter: AppDatabase? = null
    lateinit var mThreadManager: ThreadManager

    private var view: ListInterface.ListView = _view

    override fun feedPhonesList() {
        val _call = ApiInterface.getClient().getPhones()
        _call.enqueue(object : Callback<List<PhoneBean>> {
            override fun onFailure(call: Call<List<PhoneBean>>, t: Throwable) {
                Log.d("error", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<PhoneBean>>,
                response: Response<List<PhoneBean>>
            ) {
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

    override fun getPhonesList() {
        view.showPhonesList(mDataArray)
        view.hideLoading()
    }

    override fun getFavoriteItems(context: android.content.Context) {
        LocalBroadcastManager.getInstance(context).registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: android.content.Context, intent: Intent) {
                    favoriteItem.clear()
                    favoriteItem.addAll(intent.getParcelableArrayListExtra(GetFavoriteItems))
                }
            },
            IntentFilter(FavoriteItemsFromFavoriteToList)
        )
    }

    override fun sendFavoriteItems(
        context: android.content.Context,
        content: ArrayList<PhoneBean>
    ) {
        // send favorite items from list page to fragment page
        Intent(FavoriteItemsFromListToFavorite).let {
            it.putExtra(RecieveFavoriteItems, content)
            LocalBroadcastManager.getInstance(context).sendBroadcast(it)
        }
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

    override fun setupDatabase(context: android.content.Context) {
        mDatabaseAdapter = AppDatabase.getInstance(context).also {
            it.openHelper.readableDatabase
        }
    }

    override fun setupTreadManager() {
        mThreadManager = ThreadManager("database").also {
            it.start()
        }
    }
}