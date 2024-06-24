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
data class ForgotMailResponse(
    @SerializedName("idAccount") val idAccount: String?,
    override val success: Boolean,
    override val message: String?
) : BaseErrResponse(success, message)