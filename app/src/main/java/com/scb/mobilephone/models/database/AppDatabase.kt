package com.scb.mobilephone.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.scb.mobilephone.models.database.converters.ArrayListConverter
import com.scb.mobilephone.models.database.dao.FavoritesListDAO
import com.scb.mobilephone.models.database.dao.PhonesListDAO
import com.scb.mobilephone.models.database.entities.FavoritesListEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity

@Database(entities = [PhonesListEntity::class, FavoritesListEntity::class], version = 1, exportSchema = true)
@TypeConverters(value = [ArrayListConverter::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun phonesListDao(): PhonesListDAO
    abstract fun favoritesListDao(): FavoritesListDAO

    companion object {

        private var INSTANCE: AppDatabase? = null
        private val LOCK_INMEM = Any()

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(LOCK_INMEM) {
                    INSTANCE = Room.inMemoryDatabaseBuilder(context.applicationContext, AppDatabase::class.java)
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}