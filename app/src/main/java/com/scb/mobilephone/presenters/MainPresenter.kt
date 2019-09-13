package com.scb.mobilephone.presenters

import android.content.Context
import android.util.Log
import com.scb.mobilephone.extensions.PriceHL
import com.scb.mobilephone.extensions.PriceLH
import com.scb.mobilephone.extensions.RatingHL
import com.scb.mobilephone.extensions.ThreadManager
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.models.database.AppDatabase
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity
import com.scb.mobilephone.presenters.interfaces.MainInterface

class MainPresenter : MainInterface.MainPresenter {

    private var mDatabase: AppDatabase? = null
    private lateinit var mThreadManager: ThreadManager

    private var sortedList: ArrayList<PhoneBean> = ArrayList()
    private lateinit var sortedFavoritesList: List<FavoritesEntity>

    override fun sortPhonesList(list: ArrayList<PhoneBean>, sort: String) {
        sortedList.clear()
        when (sort) {
            PriceLH -> {
                sortedList.addAll(list.sortedBy { it.price })
                list.clear()
                list.addAll(sortedList)
                updatePhonesList(list)
            }
            PriceHL -> {
                sortedList.addAll(list.sortedByDescending { it.price })
                list.clear()
                list.addAll(sortedList)
                updatePhonesList(list)
            }
            RatingHL -> {
                sortedList.addAll(list.sortedByDescending { it.rating })
                list.clear()
                list.addAll(sortedList)
                updatePhonesList(list)
            }
            else -> {
                sortedList.addAll(list)
                list.clear()
                list.addAll(sortedList)
                updatePhonesList(list)
            }
        }
    }

    override fun sortFavoritesList(list: List<FavoritesEntity>, sort: String) {
        when (sort) {
            PriceLH -> {
                sortedFavoritesList = list.sortedBy { it.price }
                // updateFavoritesList(sortedFavoritesList)
            }
            PriceHL -> {
                sortedFavoritesList = list.sortedByDescending { it.price }
                // updateFavoritesList(sortedFavoritesList)
                Log.d("update", sortedFavoritesList.toString())
            }
            RatingHL -> {
                sortedFavoritesList = list.sortedByDescending { it.rating }
                // updateFavoritesList(sortedFavoritesList)
            }
        }
    }

    override fun postTask(task: Runnable) {
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
            arrayListOf<FavoritesEntity>()
        }
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

    override fun updatePhonesList(sortedList: ArrayList<PhoneBean>) {
        val sortedPhonesList = PhonesListEntity(1, sortedList)
        val task = Runnable {
            mDatabase!!.phonesListDao().updatePhonesList(sortedPhonesList)
        }
        mThreadManager.postTask(task)
    }

    override fun updateFavoritesList(sortedList: List<FavoritesEntity>) {
        val task = Runnable {
            mDatabase!!.favoritesListDao().updateData(sortedList)
            Log.d("update", mDatabase!!.favoritesListDao().queryFavoritesList().toString())
        }
        mThreadManager.postTask(task)
    }
}