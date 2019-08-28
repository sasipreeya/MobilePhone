package com.scb.mobilephone.models

import android.os.Parcel
import android.os.Parcelable

data class PhoneBean(
    val brand: String,
    val description: String,
    val id: Int,
    val name: String,
    val price: Double,
    val rating: Double,
    val thumbImageURL: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!
    )

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeValue(brand)
        p0?.writeString(description)
        p0?.writeInt(id)
        p0?.writeString(name)
        p0?.writeDouble(price)
        p0?.writeValue(rating)
        p0?.writeValue(thumbImageURL)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhoneBean> {
        override fun createFromParcel(parcel: Parcel): PhoneBean {
            return PhoneBean(parcel)
        }

        override fun newArray(size: Int): Array<PhoneBean?> {
            return arrayOfNulls(size)
        }
    }
}