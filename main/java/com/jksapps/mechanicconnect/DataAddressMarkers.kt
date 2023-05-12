package com.jksapps.mechanicconnect

import android.os.Parcel
import android.os.Parcelable

data class DataAddressMarkers(var id: String, var name: String, var businessName: String, var address: String ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(businessName)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataAddressMarkers> {
        override fun createFromParcel(parcel: Parcel): DataAddressMarkers {
            return DataAddressMarkers(parcel)
        }

        override fun newArray(size: Int): Array<DataAddressMarkers?> {
            return arrayOfNulls(size)
        }
    }
}
