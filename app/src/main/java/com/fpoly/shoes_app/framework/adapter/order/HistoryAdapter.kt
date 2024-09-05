package com.fpoly.shoes_app.framework.adapter.order

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.domain.model.history.HistoryShoe
import com.fpoly.shoes_app.utility.formatToVND

class HistoryAdapter(
    private var historyShoes: List<HistoryShoe>,
    private val onClickActive: ((HistoryShoe) -> Unit)?,
    private val onClickComplete: ((HistoryShoe) -> Unit?)?
) : RecyclerView.Adapter<HistoryAdapter.HistoryShoeViewHolder>() {

    inner class HistoryShoeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageShoeItemImageView: ImageView = itemView.findViewById(R.id.imageShoeItem)
        private val nameShoeTextView: TextView = itemView.findViewById(R.id.nameShoeItem)
        private val contentShoeTextView: TextView = itemView.findViewById(R.id.contentShoeItem)
        private val colorShoeTextView: TextView = itemView.findViewById(R.id.colorShoeItem)
        private val colorShoeItemView: CardView = itemView.findViewById(R.id.colorShoeItemView)
        private val sizeShoeTextView: TextView = itemView.findViewById(R.id.sizeShoeItem)
        private val priceShoeTextView: TextView = itemView.findViewById(R.id.priceShoeItem)
        private val rateOrTrackShoe: TextView = itemView.findViewById(R.id.rateOrTrack)
        private val wait: TextView = itemView.findViewById(R.id.wait)
        @SuppressLint("SetTextI18n")
        fun bind(historyShoe: HistoryShoe) {
            contentShoeTextView.text = historyShoe.dateOrder
            nameShoeTextView.text = historyShoe.nameOrder
            rateOrTrackShoe.text = itemView.context.getString(
                when(historyShoe.status){
                "completed"->R.string.leaveReview
                "rateCompleted"-> R.string.orderAgain
                else -> R.string.description
            })
            wait.visibility = View.GONE
            if (historyShoe.orderStatusDetails?.size==4 && historyShoe.status=="active")
                wait.visibility = View.VISIBLE

            priceShoeTextView.text = historyShoe.total.toString().formatToVND()
            Glide.with(itemView.context)
                .load(historyShoe.thumbnail)
                .placeholder(R.drawable.download) // Placeholder image
                .error(R.drawable.download) // Error image
                .into(imageShoeItemImageView)
            if (historyShoe.orderDetails?.isNotEmpty() == true) {
                val firstOrderDetail = historyShoe.orderDetails[0]
                sizeShoeTextView.text =  itemView.context.getString(R.string.size) +firstOrderDetail.size
                colorShoeItemView.backgroundTintList =  ColorStateList.valueOf(Color.parseColor(firstOrderDetail.codeColor))
                colorShoeTextView.text = itemView.context.getString(when (firstOrderDetail.textColor) {
                    "White" -> R.string.White
                    "Black" -> R.string.Black
                    "Red" -> R.string.Red
                    "Blue" -> R.string.Blue
                    else -> R.string.noData
                })
            }

            rateOrTrackShoe.setOnClickListener {
                onClickComplete?.invoke(historyShoe)
                onClickActive?.invoke(historyShoe)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryShoeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryShoeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryShoeViewHolder, position: Int) {
        holder.bind(historyShoes[position])
    }

    override fun getItemCount() = historyShoes.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateHistoryShoes(newHistoryShoes: List<HistoryShoe>) {
        historyShoes = newHistoryShoes
        notifyDataSetChanged()
    }
}