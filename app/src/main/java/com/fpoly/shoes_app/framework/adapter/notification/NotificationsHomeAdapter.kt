package com.fpoly.shoes_app.framework.adapter.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.domain.model.profile.notification.NotificationsHome

class NotificationsHomeAdapter : ListAdapter<Any, RecyclerView.ViewHolder>(NotificationsDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerText: TextView = view.findViewById(R.id.headerText)
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tittleNotification)
        val body: TextView = view.findViewById(R.id.contentNotification)
        val imageView: ImageView = view.findViewById(R.id.appCompatImageView)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is String) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notifications_home, parent, false)
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_HEADER -> {
                val header = getItem(position) as String
                (holder as HeaderViewHolder).headerText.text = header
            }
            VIEW_TYPE_ITEM -> {
                val notification = getItem(position) as NotificationsHome
                val itemViewHolder = holder as ItemViewHolder
                itemViewHolder.title.text = notification.title
                itemViewHolder.body.text = notification.body
                Glide.with(holder.itemView.context).load(notification.image).into(itemViewHolder.imageView)
            }
        }
    }
}

class NotificationsDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is String && newItem is String -> oldItem == newItem
            oldItem is NotificationsHome && newItem is NotificationsHome -> oldItem._id == newItem._id
            else -> false
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is String && newItem is String -> oldItem == newItem
            oldItem is NotificationsHome && newItem is NotificationsHome -> oldItem == newItem
            else -> false
        }
    }
}
