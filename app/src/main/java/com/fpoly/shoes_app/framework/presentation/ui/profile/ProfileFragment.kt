package com.fpoly.shoes_app.framework.presentation.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentProfileBinding
import com.fpoly.shoes_app.databinding.LayoutDialogBinding
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left) // Optional: You may need to define this animation as well
        .setPopExitAnim(R.anim.slide_out_right) // Optional: You may need to define this animation as well
        .build()
    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.constraintLogout.setOnClickListener {
            showBottomSheetDialog()
        }
        binding.constraintEdt.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment, null, navOptions)

        }
        binding.constraintAddess.setOnClickListener {
            findNavController().navigate(R.id.addressFragment, null, navOptions)
        }
        binding.constraintNotification.setOnClickListener {
            findNavController().navigate(R.id.notificationFragment, null, navOptions)
        }
        return binding.root
    }
    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogBinding: LayoutDialogBinding = LayoutDialogBinding.inflate(layoutInflater)
        dialogBinding.bottomSheetCancelButton.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        dialogBinding.bottomSheetOkButton.setOnClickListener {
            SharedPreferencesManager.removePassWord()
            val navController = findNavController()
            fragmentManager?.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            navController.navigate(R.id.loginFragmentScreen)
            bottomSheetDialog.dismiss()

        }
        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}