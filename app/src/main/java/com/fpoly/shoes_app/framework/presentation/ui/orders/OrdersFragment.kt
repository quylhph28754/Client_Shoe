package com.fpoly.shoes_app.framework.presentation.ui.orders

import androidx.core.content.ContextCompat
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentOrdersBinding
import com.fpoly.shoes_app.framework.adapter.order.ViewPagerAdapter
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding, OrdersViewModel>(
    FragmentOrdersBinding::inflate,
    OrdersViewModel::class.java
) {
    override fun setupPreViews() {

    }

    override fun setupViews() {
        val adapter = ViewPagerAdapter(this)
        (requireActivity() as MainActivity).showBottomNavigation(true)
        binding.apply { viewPager.adapter = adapter
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black_overlay))
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Active"
                1 -> "Completed"
                else -> throw IllegalStateException("Unexpected position $position")
            }        }.attach()

    }

    override fun bindViewModel() {

    }

    override fun setOnClick() {

    }
}