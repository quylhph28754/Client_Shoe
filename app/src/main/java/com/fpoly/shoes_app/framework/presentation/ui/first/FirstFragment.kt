package com.fpoly.shoes_app.framework.presentation.ui.first

import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentFirstBinding
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstFragment : BaseFragment<FragmentFirstBinding, FirstViewModel>(
    FragmentFirstBinding::inflate,
    FirstViewModel::class.java
) {
    override fun setupViews() {
        binding.buttonFirst.setOnClickListener {
            navController?.navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun bindViewModel() {

    }

    override fun setOnClick() {
    }
}