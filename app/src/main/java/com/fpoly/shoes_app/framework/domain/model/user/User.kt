package com.fpoly.shoes_app.framework.domain.model.user

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("__v") val v: Int?,
    @SerializedName("_id")val id: String?,
    val birthday: String?,
    val fullName: String?,
    val gmail: String?,
    val grender: String?,
    val imageAccount: ImageAccount?,
    val locked: Boolean?,
    val nameAccount: String?,
    val namePassword: String?,
    val otp: String?,
    val phoneNumber: String?,
    val role: Int?
)