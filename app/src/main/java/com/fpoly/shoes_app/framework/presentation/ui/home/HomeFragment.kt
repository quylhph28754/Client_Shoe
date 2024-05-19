package com.fpoly.shoes_app.framework.presentation.ui.home

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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

    @Inject
    lateinit var categoriesWithImageAdapter: CategoriesWithImageAdapter

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
    }

    override fun setOnClick() {

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