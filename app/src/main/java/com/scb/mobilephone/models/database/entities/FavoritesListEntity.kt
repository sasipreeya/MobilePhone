package com.scb.mobilephone.models.database.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.scb.mobilephone.models.PhoneBean

@Entity(tableName = "favorites_list")
data class FavoritesListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val favoritesList: ArrayList<PhoneBean>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readArrayList(PhoneBean.javaClass.classLoader) as ArrayList<PhoneBean>
    )

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeValue(id)
        parcel?.writeList(favoritesList as List<*>?)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavoritesListEntity> {
        override fun createFromParcel(parcel: Parcel): FavoritesListEntity {
            return FavoritesListEntity(parcel)
        }

        override fun newArray(size: Int): Array<FavoritesListEntity?> {
            return arrayOfNulls(size)
        }
    }
}