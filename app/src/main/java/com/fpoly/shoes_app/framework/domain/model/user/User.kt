package com.fpoly.shoes_app.framework.domain.model.user

import com.google.gson.annotations.SerializedName

data class User (
    val gmail: String?,
    val namePassword: String,
    val role: Int,
    val fullName: String?,
    val otp: String?,
    val userName: String?,
    val imageAccount: String?,
    val grender: String?,
    val phoneNumber: String?,
    @SerializedName("__v") val v: Int?,
    @SerializedName("_id")val id: String,
    val nameAccount: String,
    val locked: Boolean
)