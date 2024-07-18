package com.fpoly.shoes_app.framework.domain.model.profile.address

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Addresse(
    @SerializedName("__v")  val v: Int,
    @SerializedName("_id")val id: String,
    val detailAddress: String,
    val latitude: String,
    val longitude: String,
    val nameAddress: String,
    val permission: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(v)
        parcel.writeString(id)
        parcel.writeString(detailAddress)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
        parcel.writeString(nameAddress)
        parcel.writeString(permission)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Addresse> {
        override fun createFromParcel(parcel: Parcel): Addresse {
            return Addresse(parcel)
        }

        override fun newArray(size: Int): Array<Addresse?> {
            return arrayOfNulls(size)
        }
    }
}