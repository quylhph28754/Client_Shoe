package com.fpoly.shoes_app.framework.presentation.ui.second

import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentSecondBinding
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondFragment : BaseFragment<FragmentSecondBinding, SecondViewModel>(
    FragmentSecondBinding::inflate,
    SecondViewModel::class.java
) {
    override fun setupViews() {
        binding.buttonSecond.setOnClickListener {
            navController?.navigate(R.id.action_SecondFragment_to_ThirdFragment)
        }
    }

    override fun bindViewModel() {

    }
}