package com.scb.mobilephone.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.scb.mobilephone.models.database.dao.FavoritesListDAO
import com.scb.mobilephone.models.database.dao.PhonesListDAO
import com.scb.mobilephone.models.database.entities.FavoritesListEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity

//@Database(entities = [PhonesListEntity::class, FavoritesListEntity::class], version = 1, exportSchema = true) // migration update database
//abstract class AppDatabase : RoomDatabase() {
//
//    abstract fun phonesListDao(): PhonesListDAO
//    abstract fun favoritesListDao(): FavoritesListDAO
//
//    companion object {
//        var INSTANCE: AppDatabase? = null
//
//        fun getAppDataBase(context: Context): AppDatabase? {
//            if (INSTANCE == null){
//                synchronized(AppDatabase::class){
//                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "myDB").build()
//                }
//            }
//            return INSTANCE
//        }
//
//        fun destroyDataBase(){
//            INSTANCE = null
//        }
//    }
//}