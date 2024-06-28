package com.fpoly.shoes_app.framework.domain.model.otpConfirm

import android.os.Parcelable
import com.fpoly.shoes_app.framework.domain.model.user.User
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OtpConfirm(
    @SerializedName("id") var id: String,
    @SerializedName("otp") var otp: String
): Parcelable

data class OtpConfirmResponse(
    @SerializedName("success") var success: Boolean,
    @SerializedName("user") var user: User?,
    @SerializedName("message") var message: String?,
)