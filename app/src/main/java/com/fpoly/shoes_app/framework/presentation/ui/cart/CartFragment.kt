package com.fpoly.shoes_app.framework.presentation.ui.cart

import com.fpoly.shoes_app.databinding.FragmentCartBinding
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.checkout.CheckoutFragment

class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>(
    FragmentCartBinding::inflate,
    CartViewModel::class.java
) {
    override fun setupPreViews() {

    }
    override fun setupViews() {
        (requireActivity() as MainActivity).showBottomNavigation(true)

    }

    override fun bindViewModel() {

    }

    override fun setOnClick() {
        binding.btnGoToCheckoutFragment.setOnClickListener {
            goToCheckoutFragment()
        }
    }

    private fun goToCheckoutFragment() {
        val checkoutFragment = CheckoutFragment()
        parentFragmentManager.beginTransaction().apply {

        }
    }

}