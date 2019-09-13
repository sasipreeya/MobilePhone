package com.scb.mobilephone.models.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phones_list")
data class PhonesListEntity(

    @PrimaryKey
    var id: Int,
    var description: String,
    var brand: String,
    var name: String,
    var price: Double,
    var rating: Double,
    var thumbImageURL: String
)