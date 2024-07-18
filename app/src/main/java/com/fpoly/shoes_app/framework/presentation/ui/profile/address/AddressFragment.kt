package com.fpoly.shoes_app.framework.presentation.ui.profile.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentAddressBinding
import com.fpoly.shoes_app.framework.adapter.AddressAdapter
import com.fpoly.shoes_app.framework.data.module.CheckValidate.strNullOrEmpty
import com.fpoly.shoes_app.framework.domain.model.profile.address.Addresse
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.profile.editProfile.EditProfileViewModel
import com.fpoly.shoes_app.utility.Status
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressFragment : BaseFragment<FragmentAddressBinding, AddressViewModel>(
    FragmentAddressBinding::inflate, AddressViewModel::class.java
) {
    private lateinit var addressAdapter: AddressAdapter
    private var listAddressDetails :MutableList<Addresse>?= mutableListOf()


    private fun setupRecyclerView() {
            addressAdapter = AddressAdapter(listAddressDetails,
                // Lambda function for handling item click
                { address ->
                    val bundle = Bundle().apply {
                        putParcelable("address", address)
                    }
                    findNavController().navigate(R.id.addressDetailsFragment, bundle)
                },
                // Lambda function for handling edit click
                { address ->
                    val bundle = Bundle().apply {
                        putParcelable("address", address)
                    }
                    findNavController().navigate(R.id.editoraddFragment, bundle)
                }
            )

            binding.recycViewAddress.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = addressAdapter
            }

            // Setup ItemTouchHelper for swipe-to-delete functionality
            val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                0, // dragDirs (not used here)
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // swipeDirs
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false // No drag functionality
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    addressAdapter.deleteItem(position)
                }
            }

            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(binding.recycViewAddress)

    }


    override fun setupPreViews() {

        viewModel.allAddress( sharedPreferences.getIdUser())
    }

    override fun setupViews() {
        setupRecyclerView()
        binding.toolbar.setNavigationOnClickListener {
        findNavController().popBackStack()
    }
    }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allAddressResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        showProgressbar(false)
                        result.data?.let { addressResponse ->
                            if (addressResponse.success) {
                                listAddressDetails?.clear()
                                listAddressDetails?.addAll(addressResponse.addresses)
                                addressAdapter.notifyDataSetChanged()
                            } else {
                                StyleableToast.makeText(
                                    requireContext(), addressResponse.message, R.style.fail
                                ).show()
                            }
                        }
                    }
                    Status.ERROR -> {
                        showProgressbar(false)
                        val errorMessage = strNullOrEmpty(result.message)
                        StyleableToast.makeText(
                            requireContext(), errorMessage, R.style.fail
                        ).show()
                    }
                    Status.LOADING -> showProgressbar(true)
                    Status.INIT -> Unit
                }
                binding.btnAddAddress.isEnabled = true
            }
        }
    }


    override fun setOnClick() {
    }
}