package com.fpoly.shoes_app.framework.domain.model.login

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Login(
    @SerializedName("nameAccount") var nameAccount: String,
    @SerializedName("namePassword") var namePassword: String,
): Parcelable
data class LoginResponse(
    @SerializedName("success") var success: Boolean,
    @SerializedName("user") var user: User?,
    @SerializedName("message") var message: String?,
)
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
    val id: String,
    val nameAccount: String,
    val locked: Boolean
)