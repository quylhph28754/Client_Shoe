package com.fpoly.shoes_app.framework.data.dataremove.api.getInterface

import com.fpoly.shoes_app.framework.domain.model.profile.notification.NotificationsHomeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationsInterface {
    @GET("getListNavigationUser/{userId}")
    suspend fun getNotificationsByUser(
        @Path("userId") userId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<NotificationsHomeResponse>
}

