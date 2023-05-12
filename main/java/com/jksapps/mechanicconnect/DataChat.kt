package com.jksapps.mechanicconnect

import android.os.Parcel
import android.os.Parcelable

data class DataChat(var senderId: String, var receiverId: String, var message: String, var timeStamp: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(senderId)
        parcel.writeString(receiverId)
        parcel.writeString(message)
        parcel.writeString(timeStamp)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataChat> {
        override fun createFromParcel(parcel: Parcel): DataChat {
            return DataChat(parcel)
        }

        override fun newArray(size: Int): Array<DataChat?> {
            return arrayOfNulls(size)
        }
    }
}
