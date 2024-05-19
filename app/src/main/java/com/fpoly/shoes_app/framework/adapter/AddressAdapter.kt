package com.fpoly.shoes_app.framework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.domain.model.profile.AddressDetail

class AddressAdapter(private val addressDetails: List<AddressDetail>, private val onClick: (AddressDetail) -> Unit) :
    RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameAddress)
        val addressTextView: TextView = itemView.findViewById(R.id.Address)

        fun bind(addressDetail: AddressDetail) {
            nameTextView.text = addressDetail.name
            addressTextView.text = addressDetail.address
            itemView.setOnClickListener {
                onClick(addressDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        return AddressViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(addressDetails[position])
    }

    override fun getItemCount() = addressDetails.size
}
