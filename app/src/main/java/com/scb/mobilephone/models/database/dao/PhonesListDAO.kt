package com.scb.mobilephone.models.database.dao

import androidx.room.*
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.models.database.entities.PhonesListEntity

@Dao
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