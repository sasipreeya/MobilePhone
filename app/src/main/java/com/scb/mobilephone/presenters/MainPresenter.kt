package com.scb.mobilephone.presenters

import android.content.Context
import com.scb.mobilephone.extensions.ThreadManager
import com.scb.mobilephone.models.database.AppDatabase
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity
import com.scb.mobilephone.presenters.interfaces.MainInterface

class MainPresenter : MainInterface.MainPresenter {

    private var mDatabase: AppDatabase? = null
    private lateinit var mThreadManager: ThreadManager

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
}