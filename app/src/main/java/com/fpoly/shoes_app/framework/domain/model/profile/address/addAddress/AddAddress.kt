package com.fpoly.shoes_app.framework.domain.model.profile.address.addAddress

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddAddress(
    private val nameAddress:String,
    private val detailAddress: String,
    private val latitude: Double?,
    private val longitude: Double?,
    private val userId: String?,
): Parcelable
