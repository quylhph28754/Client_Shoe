package com.fpoly.shoes_app.framework.presentation.ui.orders.active.detailActive

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentDetailActiveBinding
import com.fpoly.shoes_app.framework.adapter.order.detail.DetailActiveAdapter
import com.fpoly.shoes_app.framework.adapter.order.detail.DetailHistoryAdapter
import com.fpoly.shoes_app.framework.domain.model.history.HistoryShoe
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.Status
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActiveFragment: BaseFragment<FragmentDetailActiveBinding, DetailActiveViewModel>(
    FragmentDetailActiveBinding::inflate,
    DetailActiveViewModel::class.java
) {
    private  var detailHistoryAdapter: DetailHistoryAdapter?=null
    private  var detailActiveAdapter: DetailActiveAdapter?=null
    private  var  historyShoe:HistoryShoe ?=null
    override fun setupPreViews() {

    }

    override fun setupViews() {
         historyShoe = arguments?.getParcelable<HistoryShoe>("history")
        detailHistoryAdapter = DetailHistoryAdapter(
            orderDetailShoes = historyShoe!!.orderDetails
        )
        detailActiveAdapter = DetailActiveAdapter(
            orderActiveDetailShoes = historyShoe!!.orderStatusDetails
        )
        binding.apply {
            recycProduct.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = detailHistoryAdapter
            }
            recycProcedure.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = detailActiveAdapter
            }
            val allViews = listOf(boxComplete, view1, transitionComplete, view2, ShiperComplete, view3, Success)
            val numberOfViewsToTint = when (historyShoe!!.orderStatusDetails?.size) {

                1,2 -> 1
                3 -> 3
                4 -> 5
                else -> allViews.size
            }
            if (historyShoe!!.orderStatusDetails?.size==4){
                comfirmTake.visibility = View.VISIBLE
            }
            val colorStateList = ContextCompat.getColorStateList(requireContext(), R.color.black)
            allViews.take(numberOfViewsToTint).forEach { view ->
                view.backgroundTintList = colorStateList
            }

        }


    }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.confirmTakeResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        showProgressbar(false)
                        val signUpResponse = result.data
                        if (signUpResponse?.order?.status ==0) {
                            findNavController().popBackStack()
                            StyleableToast.makeText(
                                requireContext(), getString(R.string.success), R.style.success
                            ).show()
                            return@collect
                        }

                    }

                    Status.ERROR -> {
                        val errorMessage = result.message ?: "Unknown error"
                        Log.e("errorMessage",errorMessage)
                        showProgressbar(false)
                    }

                    Status.LOADING -> {
                        showProgressbar(true)
                    }

                    Status.INIT -> {
                    }
                }
            }
        }

    }

    override fun setOnClick() {
        binding.apply {
            toolbar.setOnClickListener {
                findNavController().popBackStack()
            }
            comfirmTake.setOnClickListener {
                viewModel.confirmTakeDetailVM(historyShoe?._id.toString())
            }
        }


    }

}