package com.fpoly.shoes_app.framework.presentation.ui.third

import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.databinding.FragmentThirdBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdFragment : BaseFragment<FragmentThirdBinding, ThirdViewModel>(
    FragmentThirdBinding::inflate,
    ThirdViewModel::class.java
) {
    override fun setupViews() {
        binding.buttonThird.setOnClickListener {
            navController?.navigate(R.id.action_SecondFragment_to_ThirdFragment)
        }
    }

    override fun bindViewModel() {

    }

    override fun setOnClick() {

    }
}