package com.fpoly.shoes_app.framework.presentation.ui.home.notification

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fpoly.shoes_app.databinding.FragmentNotificationHomeBinding
import com.fpoly.shoes_app.framework.adapter.notification.NotificationsHomeAdapter
import com.fpoly.shoes_app.framework.domain.model.profile.notification.NotificationsHome
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
@AndroidEntryPoint
class NotificationHomeFragment : BaseFragment<FragmentNotificationHomeBinding, NotificationHomeViewModel>(
    FragmentNotificationHomeBinding::inflate,
    NotificationHomeViewModel::class.java
) {
    private lateinit var idUser: String
    private lateinit var notificationsAdapter: NotificationsHomeAdapter

    override fun setupPreViews() {
        idUser = sharedPreferences.getIdUser()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setupViews() {
        notificationsAdapter = NotificationsHomeAdapter()
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.fetchNotifications(idUser )
            }
            recycViewNotifications.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = notificationsAdapter
            }

        }
//        viewModel.fetchNotifications(idUser)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.notificationsState.collect { state ->
                binding.swipeRefreshLayout.isRefreshing = state.isLoading
                if (!state.isLoading) {
                    val groupedNotifications = groupNotificationsByDate(state.notifications.take(50))
                    notificationsAdapter.submitList(groupedNotifications)
                }
            }
        }
    }

    override fun setOnClick() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun groupNotificationsByDate(notifications: List<NotificationsHome>): List<Any> {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")
        val currentDate = LocalDate.now()
        val groupedNotifications = mutableListOf<Any>()
        notifications.groupBy { notification ->

            val notificationDate = LocalDateTime.parse(notification.time, formatter).toLocalDate()
            when {
                notificationDate.isEqual(currentDate) -> "Today"
                notificationDate.isEqual(currentDate.minusDays(1)) -> "Yesterday"
                else -> notificationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            }
        }.toSortedMap(compareByDescending { header ->
            when (header) {
                "Today" -> currentDate
                "Yesterday" -> currentDate.minusDays(1)
                else -> LocalDate.parse(header, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            }
        }).forEach { (header, notificationList) ->
            groupedNotifications.add(header) // Add the header first
            groupedNotifications.addAll(notificationList) // Add all notifications under this header
        }
        return groupedNotifications
    }
}