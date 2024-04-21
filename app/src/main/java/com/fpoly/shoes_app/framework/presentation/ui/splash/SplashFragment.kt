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

    override fun setupViews() {
        binding.viewPagerSplash.adapter = splashAdapter
        binding.dotsIndicator.attachTo(binding.viewPagerSplash)
        setOnChangeViewPager2()
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel?.uiState?.collect {
                splashAdapter.submitList(it.pagesSplash)
                binding.apply {
                    viewPagerSplash.currentItem = it.page
                    val totalPages = it.pagesSplash?.size ?: 0
                    val currentPage = it.page
                    if (currentPage >= totalPages)
                        navController?.navigate(R.id.action_SplashFragment_to_FirstFragment)
                    btnNextPager.tvButton.text =
                        if (currentPage >= totalPages - 1) GET_STARED else NEXT
                }
            }
        }
    }

    override fun setOnClick() {
        binding.btnNextPager.root.setOnClickListener {
            viewModel?.nextPage(binding.viewPagerSplash.currentItem)
        }
    }

    private fun setOnChangeViewPager2() {
        binding.viewPagerSplash.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel?.getPage(binding.viewPagerSplash.currentItem)
            }
        })
    }

    private companion object {
        const val NEXT = "Next"
        const val GET_STARED = "Get Started"
    }
}