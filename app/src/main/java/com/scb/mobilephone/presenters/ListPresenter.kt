package com.scb.mobilephone.presenters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.scb.mobilephone.extensions.*
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.models.database.AppDatabase
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity
import com.scb.mobilephone.models.network.ApiInterface
import com.scb.mobilephone.presenters.interfaces.ListInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPresenter(_view: ListInterface.ListView) : ListInterface.ListPresenter {

    private var mDatabase: AppDatabase? = null
    private lateinit var mThreadManager: ThreadManager

    private var mDataArray: ArrayList<PhoneBean> = ArrayList()
    private var view: ListInterface.ListView = _view

    override fun feedPhonesList(context: Context) {
        val call = ApiInterface.getClient().getPhones()

        call.enqueue(object : Callback<List<PhoneBean>> {
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
                    view.showPhonesList(mDataArray)
                }
            }
        })
    }

    override fun keepInDatabase(phonesList: ArrayList<PhoneBean>) {
        val task = Runnable {
            val result = mDatabase!!.phonesListDao().queryPhonesList()
            if (result == null) {
                // insert
                mDatabase!!.phonesListDao().addPhonesList(PhonesListEntity(1, phonesList))
                Log.d("database", mDatabase!!.phonesListDao().queryPhonesList().toString())
            } else {
                // update
                mDatabase!!.phonesListDao().updatePhonesList(PhonesListEntity(1, phonesList))
                Log.d("database", mDatabase!!.phonesListDao().queryPhonesList().toString())
            }
        }
        mThreadManager.postTask(task)
    }

    override fun getPhones(): ArrayList<PhoneBean> {
        return mDatabase?.let {
            it.phonesListDao().queryPhonesList()?.phonesList
        } ?: run {
            arrayListOf<PhoneBean>()
        }
    }

    override fun getFavorites(): List<FavoritesEntity> {
        return mDatabase?.favoritesListDao()?.queryFavoritesList() ?: run {
            listOf<FavoritesEntity>()
        }
    }

    override fun getPhonesList() {
        val task = Runnable {
            view.showPhonesList(mDatabase!!.phonesListDao().queryPhonesList()!!.phonesList)
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

    override fun setupDatabase(context: Context) {
        mDatabase = AppDatabase.getInstance(context).also {
            it.openHelper.readableDatabase
        }
    }

    override fun setupTreadManager() {
        mThreadManager = ThreadManager("database").also {
            it.start()
        }
    }

    override fun postTask(task: Runnable) {
        mThreadManager.postTask(task)
    }

    override fun addFavoriteItem(phone: PhoneBean) {
        val task = Runnable {
            mDatabase!!.favoritesListDao()
                .addFavorite(
                    FavoritesEntity(
                        id = phone.id,
                        brand = phone.brand,
                        description = phone.description,
                        name = phone.name,
                        price = phone.price,
                        rating = phone.rating,
                        thumbImageURL = phone.thumbImageURL
                    )
                )
            val list = mDatabase!!.favoritesListDao().queryFavoritesList()
            Log.d("database", list.toString())
            Log.d("database", "${list?.size}")
        }
        mThreadManager.postTask(task)
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