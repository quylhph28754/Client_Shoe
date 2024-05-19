package com.fpoly.shoes_app.framework.presentation.ui.home

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fpoly.shoes_app.databinding.FragmentHomeBinding
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.brands.CategoriesWithImageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate,
    HomeViewModel::class.java
) {

    @Inject
    lateinit var categoriesWithImageAdapter: CategoriesWithImageAdapter

    override fun setupViews() {
        (requireActivity() as MainActivity).showBottomNavigation(true)
        setupCategories()
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect{state ->
                categoriesWithImageAdapter.submitList(state.categories)
            }
        }
    }

    override fun setOnClick() {

    }

    private fun setupCategories() {
        binding.rcvCategory.apply {
            layoutManager = StaggeredGridLayoutManager(SPAN_COUNT_CATEGORIES, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = categoriesWithImageAdapter
        }
    }

    private companion object {
        private const val SPAN_COUNT_CATEGORIES = 4
    }
}