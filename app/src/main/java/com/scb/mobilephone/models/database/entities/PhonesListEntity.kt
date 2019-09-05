package com.scb.mobilephone.models.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.scb.mobilephone.models.PhoneBean

@Entity(tableName = "phones_list")
data class PhonesListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    var phonesList: ArrayList<PhoneBean>
)