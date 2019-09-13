package com.scb.mobilephone.presenters

import android.content.Context
import android.util.Log
import com.scb.mobilephone.extensions.PriceHL
import com.scb.mobilephone.extensions.PriceLH
import com.scb.mobilephone.extensions.RatingHL
import com.scb.mobilephone.extensions.ThreadManager
import com.scb.mobilephone.models.database.AppDatabase
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity
import com.scb.mobilephone.presenters.interfaces.MainInterface

class MainPresenter : MainInterface.MainPresenter {

    private var mDatabase: AppDatabase? = null
    private lateinit var mThreadManager: ThreadManager
    private lateinit var sortedPhonesList: List<PhonesListEntity>
    private lateinit var sortedFavoritesList: List<FavoritesEntity>

    override fun sortList(phonesList: List<PhonesListEntity>, favoriteslist: List<FavoritesEntity>, sort: String) {
        when (sort) {
            PriceLH -> {
                sortedPhonesList = phonesList.sortedBy { it.price }
                sortedFavoritesList = favoriteslist.sortedBy { it.price }
                updatePhonesList(sortedPhonesList)
                updateFavoritesList(sortedFavoritesList)
            }
            PriceHL -> {
                sortedPhonesList = phonesList.sortedByDescending { it.price }
                sortedFavoritesList = favoriteslist.sortedByDescending { it.price }
                updatePhonesList(sortedPhonesList)
                updateFavoritesList(sortedFavoritesList)
            }
            RatingHL -> {
                sortedPhonesList = phonesList.sortedByDescending { it.rating }
                sortedFavoritesList = favoriteslist.sortedByDescending { it.rating }
                updatePhonesList(sortedPhonesList)
                updateFavoritesList(sortedFavoritesList)
            }
        }
    }

    override fun postTask(task: Runnable) {
        mThreadManager.postTask(task)
    }

    override fun getPhones(): List<PhonesListEntity> {
        return mDatabase?.phonesListDao()?.queryPhonesList() ?: run {
            arrayListOf<PhonesListEntity>()
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

    override fun updatePhonesList(sortedList: List<PhonesListEntity>) {
        val task = Runnable {
            mDatabase!!.phonesListDao().updatePhonesList(sortedList)
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