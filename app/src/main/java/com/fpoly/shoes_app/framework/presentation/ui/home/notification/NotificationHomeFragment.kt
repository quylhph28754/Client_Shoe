package com.fpoly.shoes_app.framework.presentation.ui.home.notification

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.databinding.FragmentNotificationHomeBinding
import com.fpoly.shoes_app.framework.adapter.notification.NotificationsHomeAdapter
import com.fpoly.shoes_app.framework.adapter.notification.NotificationsItem
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
    private var isLoadingMore = false
    private var hasMoreData = true // Tracks if there's more data to load
    private var currentPage = 1
    private val PAGE_SIZE = 10

    override fun setupPreViews() {
        idUser = sharedPreferences.getIdUser()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setupViews() {
        notificationsAdapter = NotificationsHomeAdapter()
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                currentPage = 1
                hasMoreData = true
                notificationsAdapter.submitList(emptyList()) // Clear the list before loading new data
                viewModel.fetchNotifications(idUser, currentPage)
            }
            recycViewNotifications.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = notificationsAdapter

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val totalItemCount = layoutManager.itemCount
                        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                        if (!isLoadingMore && hasMoreData && lastVisibleItem == totalItemCount - 1) {
                            isLoadingMore = true
                            currentPage++  // Increment the page
                            viewModel.fetchNotifications(idUser, currentPage, isLoadMore = true)
                        }
                    }
                })
            }
        }
        viewModel.fetchNotifications(idUser, currentPage)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.notificationsState.collect { state ->
                binding.swipeRefreshLayout.isRefreshing = state.isLoading
                if (!state.isLoading) {
                    // Reset loading flag
                    isLoadingMore = false

                    // Check if there is more data to load
                    hasMoreData = state.notifications.size >= PAGE_SIZE

                    // Group notifications by date
                    val groupedNotifications = groupNotificationsByDate(state.notifications)

                    if (isLoadingMore) {
                        // Append new data when loading more
                        notificationsAdapter.appendData(groupedNotifications)
                    } else {
                        // Set new data when refreshing
                        notificationsAdapter.submitList(groupedNotifications)
                    }
                }

                state.errorMessage?.let {
                    showError(it)
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
    private fun groupNotificationsByDate(notifications: List<NotificationsHome>): List<NotificationsItem> {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")
        val currentDate = LocalDate.now()
        val groupedNotifications = mutableListOf<NotificationsItem>()
        val grouped = notifications.groupBy { notification ->
            try {
                val notificationDate = LocalDateTime.parse(notification.time, formatter).toLocalDate()
                when {
                    notificationDate.isEqual(currentDate) -> "Today"
                    notificationDate.isEqual(currentDate.minusDays(1)) -> "Yesterday"
                    else -> notificationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                }
            } catch (e: Exception) {
                "Unknown Date"
            }
        }.toSortedMap(compareByDescending { header ->
            when (header) {
                "Today" -> currentDate
                "Yesterday" -> currentDate.minusDays(1)
                "Unknown Date" -> LocalDate.MIN // Place unknown dates at the end
                else -> {
                    try {
                        LocalDate.parse(header, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    } catch (e: Exception) {
                        LocalDate.MIN // Handle invalid date format
                    }
                }
            }
        })

        // Add headers and notifications to the list
        grouped.forEach { (header, notificationList) ->
            groupedNotifications.add(NotificationsItem.Header(header)) // Add header (date)
            groupedNotifications.addAll(notificationList.map { NotificationsItem.NotificationItem(it) }) // Add notifications under the header
        }

        return groupedNotifications
    }

    private fun showError(message: String) {
        // Example implementation for error handling
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}
