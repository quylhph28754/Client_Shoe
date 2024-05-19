package com.fpoly.shoes_app.framework.presentation.ui.brands

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.databinding.ItemBrandWithImageViewBinding
import com.fpoly.shoes_app.framework.domain.model.Category
import com.fpoly.shoes_app.utility.loadImage
import javax.inject.Inject

private val brandsWithImageDiff = object : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem
}

class CategoriesWithImageAdapter @Inject constructor() :
    ListAdapter<Category, BrandWithImageViewHolder>(brandsWithImageDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BrandWithImageViewHolder(
            ItemBrandWithImageViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: BrandWithImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class BrandWithImageViewHolder(
    private val binding: ItemBrandWithImageViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(brand: Category) {
        binding.run {
            imgBrand.loadImage(brand.image)
            tvBrand.text = brand.name
        }
    }
}