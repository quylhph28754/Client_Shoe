package com.fpoly.shoes_app.framework.data.dataremove.api.getInterface

import com.fpoly.shoes_app.framework.domain.model.profile.notification.NotificationsHome
import retrofit2.http.GET
import retrofit2.http.Path

interface NotificationsInterface {
    @GET("getListNavigationUser/{userId}")
    suspend fun getNotifications(
        @Path("userId") userId: String
    ): List<NotificationsHome>
}