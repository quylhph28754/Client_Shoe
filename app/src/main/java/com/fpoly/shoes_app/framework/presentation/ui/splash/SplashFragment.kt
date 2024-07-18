package com.fpoly.shoes_app.framework.presentation.ui.splash

import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentSplashBinding
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>(
    FragmentSplashBinding::inflate,
    SplashViewModel::class.java
) {

    @Inject
    lateinit var splashAdapter: SplashAdapter
    override fun setupPreViews() {

    }
    override fun setupViews() {
        binding.viewPagerSplash.adapter = splashAdapter
        binding.dotsIndicator.attachTo(binding.viewPagerSplash)
        setOnChangeViewPager2()
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect {state ->
                splashAdapter.submitList(state.pagesSplash)
                binding.apply {
                    viewPagerSplash.currentItem = state.page
                    btnNextPager.tvButton.text = state.textButton
                }
            }
        }

        lifecycleScope.launch {
            viewModel.singleEvent.collect { event ->
                when (event) {
                    is SplashSingleEvent.NavigateToNextScreen ->
                        navController?.navigate(R.id.action_slashFragment_to_loginFragmentScreen)
                }
            }
        }
    }

    override fun setOnClick() {
        binding.btnNextPager.root.setOnClickListener {
            viewModel.nextPage(binding.viewPagerSplash.currentItem, splashAdapter.itemCount)
        }
    }

    private fun setOnChangeViewPager2() {
        binding.viewPagerSplash.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.getPage(binding.viewPagerSplash.currentItem, splashAdapter.itemCount)
            }
        })
    }
}