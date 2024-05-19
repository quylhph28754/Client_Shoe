package com.fpoly.shoes_app.framework.presentation.ui.profile

import com.fpoly.shoes_app.databinding.FragmentProfileBinding
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(
    FragmentProfileBinding::inflate,
    ProfileViewModel::class.java
) {
    override fun setupViews() {
        (requireActivity() as MainActivity).showBottomNavigation(true)

    }

    override fun bindViewModel() {

    }

    override fun setOnClick() {

    }
}