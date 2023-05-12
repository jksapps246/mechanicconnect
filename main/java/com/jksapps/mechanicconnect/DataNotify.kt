package com.jksapps.mechanicconnect

import android.os.Parcel
import android.os.Parcelable

data class DataNotify(var userId: String, var heading: String, var desc: String, var image: Int, var action: String, var userData: String ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(heading)
        parcel.writeString(desc)
        parcel.writeInt(image)
        parcel.writeString(action)
        parcel.writeString(userData)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataNotify> {
        override fun createFromParcel(parcel: Parcel): DataNotify {
            return DataNotify(parcel)
        }

        override fun newArray(size: Int): Array<DataNotify?> {
            return arrayOfNulls(size)
        }
    }
}
