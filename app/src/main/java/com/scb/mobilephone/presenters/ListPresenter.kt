package com.scb.mobilephone.presenters

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.scb.mobilephone.extensions.*
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.models.database.AppDatabase
import com.scb.mobilephone.models.database.entities.FavoritesListEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity
import com.scb.mobilephone.models.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPresenter(_view: ListInterface.ListView) : ListInterface.ListPresenter {

    companion object {
        var mDatabaseAdapter: AppDatabase? = null
        lateinit var mThreadManager: ThreadManager
        var favoriteItem: ArrayList<PhoneBean> = ArrayList()
    }

    private var mDataArray: ArrayList<PhoneBean> = ArrayList()
    private var view: ListInterface.ListView = _view

    override fun feedPhonesList(context: Context) {
        val _call = ApiInterface.getClient().getPhones()
        _call.enqueue(object : Callback<List<PhoneBean>> {
            override fun onFailure(call: Call<List<PhoneBean>>, t: Throwable) {
                Toast.makeText(context, "Fail to get data from API", Toast.LENGTH_LONG).show()
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
                    Log.d("data", mDataArray.toString())
                    keepInDatabase(mDataArray)
                }
            }
        })
    }

    override fun keepInDatabase(phonesList: ArrayList<PhoneBean>) {
        val task = Runnable {
            val result = mDatabaseAdapter!!.phonesListDao().queryPhonesList()
            if (result == null) {
                // insert
                mDatabaseAdapter!!.phonesListDao().addPhonesList(PhonesListEntity(1, phonesList))
                Log.d("database", mDatabaseAdapter!!.phonesListDao().queryPhonesList().toString())
            } else {
                // update
                mDatabaseAdapter!!.phonesListDao().updatePhonesList(PhonesListEntity(1, phonesList))
                Log.d("database", mDatabaseAdapter!!.phonesListDao().queryPhonesList().toString())
            }
        }
        mThreadManager.postTask(task)
    }

    override fun getPhonesList() {
        val task = Runnable {
            view.showPhonesList(mDatabaseAdapter!!.phonesListDao().queryPhonesList()!!.phonesList)
        }
        mThreadManager.postTask(task)
        view.hideLoading()
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

    override fun setupDatabase(context: Context) {
        mDatabaseAdapter = AppDatabase.getInstance(context).also {
            it.openHelper.readableDatabase
        }
    }

    override fun setupTreadManager() {
        mThreadManager = ThreadManager("database").also {
            it.start()
        }
    }

    override fun updateFavoritesList(favoritesList: ArrayList<PhoneBean>) {
        val task = Runnable {
            val result = mDatabaseAdapter!!.favoritesListDao().queryFavoritesList()
            if (result == null) {
                mDatabaseAdapter!!.favoritesListDao().addFavoritesList(FavoritesListEntity(null, favoritesList))
            } else {
                mDatabaseAdapter!!.favoritesListDao().updateFavoritesList(FavoritesListEntity(1, favoritesList))
            }
            Log.d("database", mDatabaseAdapter!!.favoritesListDao().queryFavoritesList().toString())
        }
        mThreadManager.postTask(task)
    }
}