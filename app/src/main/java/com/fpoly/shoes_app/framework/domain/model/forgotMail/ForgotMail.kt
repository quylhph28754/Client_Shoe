package com.fpoly.shoes_app.framework.domain.model.forgotMail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForgotMail(
    @SerializedName("nameAccount") val nameAccount: String,
): Parcelable

open class BaseErrResponse(
    @SerializedName("success") open val success: Boolean,
    @SerializedName("message") open val message: String?,
)
 class ForgotMailResponse(
    @SerializedName("idAccount") val idAccount: String?,
     success: Boolean,
     message: String?
) : BaseErrResponse(success, message)