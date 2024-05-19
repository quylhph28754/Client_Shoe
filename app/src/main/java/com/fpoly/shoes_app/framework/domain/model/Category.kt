package com.fpoly.shoes_app.framework.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: String? = null,
    val image: String? = null,
    val name: String? = null
) : Parcelable