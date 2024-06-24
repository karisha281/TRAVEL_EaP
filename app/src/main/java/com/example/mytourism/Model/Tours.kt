package com.example.mytourism.Model

import android.os.Parcel
import android.os.Parcelable

data class Tour(
    val id: Int,
    val name: String,
    val days: Int,
    val hotel: String,
    val description: String,
    val price: Int,
    val imageResource: Int,
    val currency: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(days)
        parcel.writeString(hotel)
        parcel.writeString(description)
        parcel.writeInt(price)
        parcel.writeInt(imageResource)
        parcel.writeString(currency)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tour> {
        override fun createFromParcel(parcel: Parcel): Tour {
            return Tour(parcel)
        }

        override fun newArray(size: Int): Array<Tour?> {
            return arrayOfNulls(size)
        }
    }
}

object Constants {
    const val CURRENCY = "RUB"
}