package com.fpoly.shoes_app.framework.presentation.ui.home.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.profile.notification.NotificationsHome
import com.fpoly.shoes_app.framework.repository.NotificationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationHomeViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    private val _notificationsState = MutableStateFlow(NotificationsUiState())
    val notificationsState: StateFlow<NotificationsUiState> = _notificationsState

    fun fetchNotifications(userId: String, currentPage: Int, isLoadMore: Boolean = false) {
        viewModelScope.launch {
            if (!isLoadMore) _notificationsState.value = NotificationsUiState(isLoading = true)

            val result = notificationsRepository.loadNotifications(userId, currentPage)
            result.onSuccess { response ->
                val updatedNotifications = if (isLoadMore) {
                    _notificationsState.value.notifications + response.notifications
                } else {
                    response.notifications
                }

                _notificationsState.value = NotificationsUiState(
                    notifications = updatedNotifications,
                    isLoading = false,
                    hasMoreData = response.notifications.size == PAGE_SIZE,
                    isLoadMore = isLoadMore
                )
            }.onFailure { exception ->
                _notificationsState.value = NotificationsUiState(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    data class NotificationsUiState(
        val isLoading: Boolean = false,
        val notifications: List<NotificationsHome> = emptyList(),
        val errorMessage: String? = null,
        val hasMoreData: Boolean = true,
        val isLoadMore: Boolean = false
    )

    companion object {
        private const val PAGE_SIZE = 10
    }
}
