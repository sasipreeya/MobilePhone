package com.scb.mobilephone.models.database.converters

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder

abstract class BaseListConverter<PnoneBean> {

    @TypeConverter
    abstract fun fromString(value: String): ArrayList<PnoneBean>

    @TypeConverter
    fun fromArrayList(value: ArrayList<PnoneBean>): String {
        val json = GsonBuilder().create().toJson(value)
        return json ?: ""
    }
}