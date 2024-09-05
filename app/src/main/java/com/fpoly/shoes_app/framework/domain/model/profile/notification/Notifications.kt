package com.fpoly.shoes_app.framework.domain.model.profile.notification

data class NotificationsHome(
    val __v: Int?,
    val _id: String?,
    val body: String?,
    val image: String?,
    val time: String?,
    val title: String?,
    val typeNotification: String?,
    val userId: String?
)
//@Parcelize
//data class Notifications(
//    @SerializedName("userId") val userId: String,
//): Parcelable

//class NotificationsHomeResponse(val notification: List<NotificationsHome>?)