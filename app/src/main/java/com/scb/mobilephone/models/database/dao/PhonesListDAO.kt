package com.scb.mobilephone.models.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.scb.mobilephone.models.database.entities.PhonesListEntity

interface PhonesListDAO {

    // phones_list
    @Query("select phoneslist from phones_list")
    fun queryPhonesList(): PhonesListEntity?

    @Insert
    fun addPhonesList(phoneslist: PhonesListEntity)

    @Update
    fun updatePhonesList(phoneslist: PhonesListEntity)

    @Delete
    fun deletePhonesList(phoneslist: PhonesListEntity)
}