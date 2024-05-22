package com.fpoly.shoes_app.framework.domain.model.signUp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUp(
    @SerializedName("nameAccount") var nameAccount: String,
    @SerializedName("namePassword") var namePassword: String,
): Parcelable
data class SignUpResponse(
    @SerializedName("success") var success: Boolean,
    @SerializedName("message") var message: String?,
)