package com.fpoly.shoes_app.framework.domain.model.updateRate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateRate(
    val commentText: String,
    val oderId: List<String>,
    val rateNumber: Int,
    val shoeId: List<String>,
    val userId: String
): Parcelable
