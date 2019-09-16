package com.scb.mobilephone.models.database.converters

import androidx.room.TypeConverter

abstract class BaseListConverter<PhoneBean> {

    @TypeConverter
    abstract fun fromString(value: String): ArrayList<PhoneBean>
}