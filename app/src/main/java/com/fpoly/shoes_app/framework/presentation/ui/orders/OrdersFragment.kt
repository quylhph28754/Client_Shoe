package com.fpoly.shoes_app.framework.presentation.ui.orders

import com.fpoly.shoes_app.databinding.FragmentOrdersBinding
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment

class OrdersFragment : BaseFragment<FragmentOrdersBinding, OrdersViewModel>(
    FragmentOrdersBinding::inflate,
    OrdersViewModel::class.java
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