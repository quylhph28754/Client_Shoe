package com.fpoly.shoes_app.framework.domain.model.login

import android.os.Parcelable
import com.fpoly.shoes_app.framework.domain.model.forgotMail.BaseErrResponse
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Login(
    @SerializedName("nameAccount") var nameAccount: String,
    @SerializedName("namePassword") var namePassword: String,
): Parcelable
 data class LoginResponse(
     @SerializedName("user") val user: User?, override val success: Boolean, override val message: String?,

     ):BaseErrResponse(success, message)


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