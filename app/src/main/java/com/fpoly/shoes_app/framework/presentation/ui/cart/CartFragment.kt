package com.fpoly.shoes_app.framework.presentation.ui.cart

import com.fpoly.shoes_app.databinding.FragmentCartBinding
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment

class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>(
    FragmentCartBinding::inflate,
    CartViewModel::class.java
) {
    override fun setupViews() {
        (requireActivity() as MainActivity).showBottomNavigation(true)

    }

    override fun bindViewModel() {

    }

    override fun setOnClick() {

    }
}