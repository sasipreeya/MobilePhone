package com.scb.mobilephone.models.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoritesEntity(

    @PrimaryKey
    val id: Int,
    val description: String,
    val brand: String,
    val name: String,
    val price: Double,
    val rating: Double,
    val thumbImageURL: String

)