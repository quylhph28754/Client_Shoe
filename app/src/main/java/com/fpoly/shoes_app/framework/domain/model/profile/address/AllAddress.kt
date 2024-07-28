package com.fpoly.shoes_app.framework.domain.model.profile.address

import com.fpoly.shoes_app.framework.domain.model.user.User
import com.google.gson.annotations.SerializedName

data class AllAddressResponse(
    val addresses: List<Addresse>?,
    val message: String,
    val success: Boolean
)
