package com.fpoly.shoes_app.framework.presentation.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentProfileBinding
import com.fpoly.shoes_app.databinding.LayoutDialogBinding
import com.fpoly.shoes_app.framework.data.othetasks.AddImage
import com.fpoly.shoes_app.framework.domain.model.profile.ProfileResponse
import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccount
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(
    FragmentProfileBinding::inflate, ProfileViewModel::class.java
) {
    private var uriPath :Uri?=null
    private var idUser = ""
    private val navOptions =
        NavOptions.Builder().setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left).setPopExitAnim(R.anim.slide_out_right).build()

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogBinding: LayoutDialogBinding = LayoutDialogBinding.inflate(layoutInflater)
        dialogBinding.bottomSheetCancelButton.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        dialogBinding.bottomSheetOkButton.setOnClickListener {
            sharedPreferences.setUserWait()
            sharedPreferences.removeUser()
            sharedPreferences.removeIdUser()
            val navController = findNavController()
            fragmentManager?.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            navController.navigate(R.id.loginFragmentScreen)
            Log.e("userWait", sharedPreferences.getUserNameWait())
            Log.e("user", sharedPreferences.getUserName())
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }

    override fun setupPreViews() {
    }

    override fun setupViews() {
        (requireActivity() as MainActivity).showBottomNavigation(true)
        idUser = sharedPreferences.getIdUser()
        viewModel.profilefind(idUser)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setUpResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val signUpResponse = result.data
                        if (signUpResponse?.success == true) {
                            StyleableToast.makeText(
                                requireContext(), getString(R.string.success), R.style.success
                            ).show()
                            service.playNotificationSound(requireContext())
                        }

                    }

                    Status.ERROR -> {
                        val errorMessage = result.message ?: "Unknown error"
                        Log.e("error", errorMessage)

                    }

                    Status.LOADING -> {
                    }

                    Status.INIT -> {
                    }
                }
            }
            return@launch
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.findProfileResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> handleSuccess(result.data)
                    Status.ERROR -> handleError(result.message)
                    Status.LOADING -> handleLoading()
                    Status.INIT -> {}
                }
            }

        }
    }

    private fun handleSuccess(profileResponse: ProfileResponse?) {
        Log.e("profileResponse", profileResponse.toString())
        profileResponse?.user?.let { user ->
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.hideShimmer()
            Glide.with(requireContext())
                .load( user.imageAccount)
                .placeholder(R.drawable.baseline_account_circle_24)
                .error(R.drawable.baseline_account_circle_24)
                .circleCrop() // Bo tròn ảnh
                .into(binding.idAvatar);
            binding.nameProfile.text = user.fullName ?: getString(R.string.name)
            binding.phoneProfile.text = user.phoneNumber ?: getString(R.string.phone_suggest)
        }
    }

    private fun handleError(errorMessage: String?) {
        Log.e("ProfileFragment", "Profile error: $errorMessage")
    }

    private fun handleLoading() {
        binding.let {
            CoroutineScope(Dispatchers.Main).launch {
                it.shimmerLayout.startShimmer()
            }
        }
    }

    override fun setOnClick() {
        binding.constraintLogout.setOnClickListener {
            showBottomSheetDialog()
        }
        binding.constraintEdt.setOnClickListener {
            findNavController().navigate(R.id.setUpAccountFragment, null, navOptions)
        }
        binding.constraintAddess.setOnClickListener {
            findNavController().navigate(R.id.addressFragment, null, navOptions)
        }
        binding.constraintNotification.setOnClickListener {
            findNavController().navigate(R.id.notificationFragment, null, navOptions)
        }
        binding.relative.setOnClickListener {
            AddImage.openImageDialog(requireContext(), requireActivity(), this@ProfileFragment)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
             uriPath = AddImage.handleImageSelectionResult(requestCode, resultCode, data)
            uriPath?.let {
                Glide.with(requireContext())
                    .load(it)
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .error(R.drawable.baseline_account_circle_24)
                    .circleCrop() // Bo tròn ảnh
                    .into(binding.idAvatar);
//                binding.idAvatar.setImageURI(it) // Set the image URI to the ImageView
            }
            viewModel.setUp(idUser, SetUpAccount(uriPath.toString(), "", "", "", "", ""))
        }
    }
}
