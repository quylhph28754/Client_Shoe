package com.fpoly.shoes_app.framework.presentation.ui.home

import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentHomeBinding
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.brands.CategoriesWithImageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate,
    HomeViewModel::class.java
) {
    private val navOptions =
        NavOptions.Builder().setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left).setPopExitAnim(R.anim.slide_out_right).build()
    @Inject
    lateinit var categoriesWithImageAdapter: CategoriesWithImageAdapter
    override fun setupPreViews() {

    }
    override fun setupViews() {
        (requireActivity() as MainActivity).showBottomNavigation(true)
        setupCategories()
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.mapNotNull {
                it.categories
            }.distinctUntilChanged().collect {
                categoriesWithImageAdapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.mapNotNull {
                it.isLoading
            }.distinctUntilChanged().collect {
                showProgressbar(it)
            }
        }
    }

    override fun setOnClick() {
        binding.notificationHome.setOnClickListener {
            val fragmentId = R.id.notificationHomeFragment
            val navController = findNavController()
            val currentDestination = navController.currentDestination
            if (currentDestination == null || currentDestination.id != fragmentId) {
                navController.navigate(fragmentId, null, navOptions)
            }        }
    }

    private fun setupCategories() {
        binding.rcvCategory.run {
            layoutManager = StaggeredGridLayoutManager(
                SPAN_COUNT_CATEGORIES,
                StaggeredGridLayoutManager.VERTICAL
            )
            adapter = categoriesWithImageAdapter
        }
    }

    private companion object {
        private const val SPAN_COUNT_CATEGORIES = 4
    }
}