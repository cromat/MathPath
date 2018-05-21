package com.example.cromat.mathpath.model

import android.os.Parcel
import android.os.Parcelable

class PetItem(
        var name: String,
        var price: Int,
        var permanent: Boolean,
        var bought: Boolean,
        var activated: Boolean,
        var picture: Int,
        var bindedElementId: Int?
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(price)
        parcel.writeByte(if (permanent) 1 else 0)
        parcel.writeByte(if (bought) 1 else 0)
        parcel.writeByte(if (activated) 1 else 0)
        parcel.writeInt(picture)
        if (bindedElementId != null) {
            parcel.writeInt(bindedElementId!!)
        }
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
