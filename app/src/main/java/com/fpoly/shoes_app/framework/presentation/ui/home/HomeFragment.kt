package com.fpoly.shoes_app.framework.presentation.ui.home

import com.fpoly.shoes_app.databinding.FragmentHomeBinding
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate,
    HomeViewModel::class.java
) {
    override fun setupViews() {
        (requireActivity() as MainActivity).showBottomNavigation(true)

    }

    override fun bindViewModel() {

    }

    override fun setOnClick() {

    }
}