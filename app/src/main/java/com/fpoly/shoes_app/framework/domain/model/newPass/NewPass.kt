package com.fpoly.shoes_app.framework.domain.model.newPass

import android.os.Parcelable
import com.fpoly.shoes_app.framework.domain.model.forgotMail.BaseErrResponse
import com.fpoly.shoes_app.framework.domain.model.user.User
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewPass(
    @SerializedName("newPassword") val newPassword: String
): Parcelable


class NewPassResponse(
    success: Boolean,
    message: String?
) : BaseErrResponse(success, message)