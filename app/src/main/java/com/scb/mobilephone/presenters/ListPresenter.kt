package com.scb.mobilephone.presenters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.scb.mobilephone.extensions.ThreadManager
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

    private var mDataArray: List<PhonesListEntity> = listOf()
    private var view: ListInterface.ListView = _view

    override fun feedPhonesList(context: Context) {
        val call = ApiInterface.getClient().getPhones()

        call.enqueue(object : Callback<List<PhonesListEntity>> {
            override fun onFailure(call: Call<List<PhonesListEntity>>, t: Throwable) {
                Toast.makeText(context, "Fail to get data from API", Toast.LENGTH_LONG).show()
                Log.d("error", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<PhonesListEntity>>,
                response: Response<List<PhonesListEntity>>
            ) {
                Log.d("response", response.body().toString())
                if (response.isSuccessful) {
                    val responseAPI = response.body()
                    mDataArray = responseAPI!!
                    view.showPhonesList(mDataArray)
                    view.hideLoading()
                    Log.d("data", mDataArray.toString())
                    keepInDatabase(mDataArray)
                    view.showPhonesList(mDataArray)
                }
            }
        })
    }

    override fun keepInDatabase(phonesList: List<PhonesListEntity>) {
        val task = Runnable {
            val result = mDatabase!!.phonesListDao().queryPhonesList()
            if (result!!.isEmpty()) {
                // insert
                for (i in phonesList.indices) {
                    mDatabase!!.phonesListDao().addPhonesList(phonesList[i])
                }
            }
            Log.d("database", result.toString())
        }
        mThreadManager.postTask(task)
    }

    override fun getPhones(): List<PhonesListEntity> {
        return mDatabase?.phonesListDao()?.queryPhonesList() ?: run {
            listOf<PhonesListEntity>()
        }
    }

    override fun getFavorites(): List<FavoritesEntity> {
        return mDatabase?.favoritesListDao()?.queryFavoritesList() ?: run {
            listOf<FavoritesEntity>()
        }
    }

    override fun getPhonesList() {
        val task = Runnable {
            view.showPhonesList(mDatabase!!.phonesListDao().queryPhonesList())
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

    override fun addFavoriteItem(phone: PhonesListEntity) {
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
        }
        mThreadManager.postTask(task)
    }

    override fun removeFavoriteItem(id: Int) {
        val task = Runnable {
            mDatabase!!.favoritesListDao()
                .deleteFavorite(id)
        }
        mThreadManager.postTask(task)
    }
}