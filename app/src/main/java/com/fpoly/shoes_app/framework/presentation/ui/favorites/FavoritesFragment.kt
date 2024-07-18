package com.fpoly.shoes_app.framework.presentation.ui.favorites

import com.fpoly.shoes_app.databinding.FragmentFavoritesBinding
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding, FavoritesViewModel>(
    FragmentFavoritesBinding::inflate,
    FavoritesViewModel::class.java
) {
    override fun setupPreViews() {

    }
    override fun setupViews() {
        (requireActivity() as MainActivity).showBottomNavigation(true)

    }

    override fun bindViewModel() {

    }

    override fun setOnClick() {

    }
}