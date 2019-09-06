package com.scb.mobilephone.models.database.converters

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder

abstract class BaseListConverter<PhoneBean> {

    @TypeConverter
    abstract fun fromString(value: String): ArrayList<PhoneBean>

    @TypeConverter
    fun fromArrayList(value: ArrayList<PhoneBean>): String {
        val json = GsonBuilder().create().toJson(value)
        return json ?: ""
    }
}