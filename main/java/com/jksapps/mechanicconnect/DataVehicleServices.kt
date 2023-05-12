package com.jksapps.mechanicconnect

import android.os.Parcel
import android.os.Parcelable

data class DataVehicleServices(var sImage: Int, var sName: String ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(sImage)
        parcel.writeString(sName)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataVehicleServices> {
        override fun createFromParcel(parcel: Parcel): DataVehicleServices {
            return DataVehicleServices(parcel)
        }

        override fun newArray(size: Int): Array<DataVehicleServices?> {
            return arrayOfNulls(size)
        }
    }
}
