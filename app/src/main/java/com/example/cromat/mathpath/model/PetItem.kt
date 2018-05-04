package com.example.cromat.mathpath.model

import android.os.Parcel
import android.os.Parcelable

class PetItem(
        val name: String,
        val price: Int,
        val permanent: Boolean,
        val bought: Boolean,
        val activated: Boolean,
        val picture: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(price)
        parcel.writeByte(if (permanent) 1 else 0)
        parcel.writeByte(if (bought) 1 else 0)
        parcel.writeByte(if (activated) 1 else 0)
        parcel.writeString(picture)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PetItem> {
        override fun createFromParcel(parcel: Parcel): PetItem {
            return PetItem(parcel)
        }

        override fun newArray(size: Int): Array<PetItem?> {
            return arrayOfNulls(size)
        }
    }
}
