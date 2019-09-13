package com.scb.mobilephone.models.database.dao

import androidx.room.*
import com.scb.mobilephone.models.database.entities.PhonesListEntity

@Dao
interface PhonesListDAO {

    @Query("select * from phones_list")
    fun queryPhonesList(): List<PhonesListEntity>?

    @Insert
    fun addPhonesList(phoneslist: PhonesListEntity)

    @Update
    fun updatePhonesList(phoneslist: List<PhonesListEntity>)

    @Transaction
    fun updateData(favorites: List<PhonesListEntity>) {
        deleteAllFavorite()
        insertAll(favorites)
    }

    @Insert
    fun insertAll(favorites: List<PhonesListEntity>)

    @Query("DELETE FROM phones_list")
    fun deleteAllFavorite()
}