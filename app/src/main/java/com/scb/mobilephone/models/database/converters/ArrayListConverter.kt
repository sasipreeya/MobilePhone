package com.scb.mobilephone.models.database.converters

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.scb.mobilephone.models.PhoneBean
import java.lang.reflect.ParameterizedType

class ArrayListConverter: BaseListConverter<PhoneBean>() {

    @TypeConverter
    override fun fromString(value: String): ArrayList<PhoneBean> {
        val typeToken = object : TypeToken<ArrayList<PhoneBean>>() {}
        val type = typeToken.type as ParameterizedType
        val list = GsonBuilder().create().fromJson<ArrayList<PhoneBean>>(value, type)
        return list ?: ArrayList()
    }
}