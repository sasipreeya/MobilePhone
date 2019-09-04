package com.scb.mobilephone.models.database.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.scb.mobilephone.models.PhoneBean

@Entity(tableName = "phones_list")
data class PhonesListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    var phonesList: ArrayList<PhoneBean>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readArrayList(PhoneBean.javaClass.classLoader) as ArrayList<PhoneBean>
    )

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeValue(id)
        parcel?.writeList(phonesList as ArrayList<*>?)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhonesListEntity> {
        override fun createFromParcel(parcel: Parcel): PhonesListEntity {
            return PhonesListEntity(parcel)
        }

        override fun newArray(size: Int): Array<PhonesListEntity?> {
            return arrayOfNulls(size)
        }
    }
}