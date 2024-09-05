package com.fpoly.shoes_app.framework.presentation.ui.home.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.data.dataremove.api.getInterface.NotificationsInterface
import com.fpoly.shoes_app.framework.domain.model.profile.notification.NotificationsHome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationHomeViewModel @Inject constructor(
    private val notificationsInterface: NotificationsInterface
) : ViewModel() {

    private val _notificationsState = MutableStateFlow(NotificationsUiState())
    val notificationsState: StateFlow<NotificationsUiState> = _notificationsState

    fun fetchNotifications(userId: String) {
        viewModelScope.launch {
            try {
                _notificationsState.value = NotificationsUiState(isLoading = true)
                val notifications = notificationsInterface.getNotifications(userId)
                _notificationsState.value = NotificationsUiState(notifications = notifications)
            } catch (e: Exception) {
                _notificationsState.value = NotificationsUiState(errorMessage = e.message)
            }
        }
    }

    data class NotificationsUiState(
        val isLoading: Boolean = false,
        val notifications: List<NotificationsHome> = emptyList(),
        val errorMessage: String? = null
    )
}