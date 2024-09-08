package com.fpoly.shoes_app.framework.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.getInterface.NotificationsInterface
import com.fpoly.shoes_app.framework.domain.model.profile.notification.NotificationsHomeResponse
import javax.inject.Inject

class NotificationsRepository @Inject constructor(
    private val notificationsInterface: NotificationsInterface
) {

    private val limit = 10 // Items per page
    private var isLastPage = false // Track if last page has been reached

    suspend fun loadNotifications(userId: String, currentPage:Int): Result<NotificationsHomeResponse> {
        if (isLastPage ) {
            return Result.failure(Exception("No more notifications to load"))
        }

        // If this is not a load more request, reset the page
        if (currentPage>0) {
            isLastPage = false // Reset last page flag on fresh load
        }

        return try {
            val response = notificationsInterface.getNotificationsByUser(
                userId = userId,
                page=currentPage,
                limit=limit
            )

            if (response.isSuccessful) {
                val notificationResponse = response.body()
                if (notificationResponse != null) {
                    // If the response is less than the limit, mark it as the last page
                    if (notificationResponse.totalPages < currentPage) {
                        isLastPage = true
                    }

                    Result.success(notificationResponse)
                } else {
                    Result.failure(Exception("Empty response"))
                }
            } else {
                Result.failure(Exception("Failed to load notifications"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
