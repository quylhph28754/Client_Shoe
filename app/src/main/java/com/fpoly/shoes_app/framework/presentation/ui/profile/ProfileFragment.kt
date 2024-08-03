package com.fpoly.shoes_app.framework.presentation.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentProfileBinding
import com.fpoly.shoes_app.databinding.LayoutDialogBinding
import com.fpoly.shoes_app.framework.data.othetasks.AddImage
import com.fpoly.shoes_app.framework.data.othetasks.AddImage.Glides
import com.fpoly.shoes_app.framework.domain.model.profile.ProfileResponse
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.setUpAccount.SetUpAccountViewModel
import com.fpoly.shoes_app.utility.Imagesss
import com.fpoly.shoes_app.utility.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, SetUpAccountViewModel>(
    FragmentProfileBinding::inflate, SetUpAccountViewModel::class.java
) {
    private var uriPath :Uri?=null
    private var imageShow :String?=null
    private lateinit var captureImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var idUser: String
    private var bmp: Bitmap? = null
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
            childFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            navController.navigate(R.id.loginFragmentScreen)
            Log.e("userWait", sharedPreferences.getUserNameWait())
            Log.e("user", sharedPreferences.getUserName())
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }

    override fun setupPreViews() {
        captureImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uriPath = AddImage.imageUri
                handleResult(uriPath)
            }
        }

        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uriPath = AddImage.handleImageSelectionResult(result.data)
                handleResult(uriPath)
            }
        }
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
                            imageShow = signUpResponse.user?.imageAccount?.`$binary`?.base64
//                            StyleableToast.makeText(
//                                requireContext(), getString(R.string.success), R.style.success
//                            ).show()
//                            service.playNotificationSound(requireContext())
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

    private fun handleResult(uri: Uri?) {
        val context = requireContext()
        val bitmap = uri?.let { AddImage.rotateImageIfRequired(context, it) }
        bitmap?.let {
            Glides(it, context, binding.idAvatar)
        }
        val imagePath = uri?.let { File(Imagesss.getPathFromUri(it, context)) }
        viewModel.setUp(idUser, imagePath, null, null, null, null, null)
    }
    private fun handleSuccess(profileResponse: ProfileResponse?) {
        Log.e("profileResponse", profileResponse.toString())
        profileResponse?.user?.let { user ->
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.hideShimmer()
            val dataImage =user.imageAccount?.`$binary`?.base64.toString()
            imageShow = dataImage
            val decodeDataImg = Base64.decode(dataImage, Base64.DEFAULT)
            bmp = BitmapFactory.decodeByteArray(decodeDataImg, 0, decodeDataImg.size)
            bmp?.let { Glides(it,requireContext(),binding.idAvatar) }
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
            navigateToFragment(R.id.editProfileFragment)
        }
        binding.constraintAddess.setOnClickListener {
            navigateToFragment(R.id.addressFragment)
        }
        binding.constraintNotification.setOnClickListener {
            navigateToFragment(R.id.generalSettingFragment)
        }
        binding.relative.setOnClickListener {
                AddImage.openImageDialog(imageShow,requireContext(), requireActivity()) { intent ->
                    when (intent.action) {
                        MediaStore.ACTION_IMAGE_CAPTURE -> captureImageLauncher.launch(intent)
                        Intent.ACTION_PICK -> pickImageLauncher.launch(intent)
                    }
                }

        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == Activity.RESULT_OK) {
//            uriPath = AddImage.handleImageSelectionResult( data)
//            val imagePath = uriPath?.let { File(Imagesss.getPathFromUri(it, requireContext())) }
//            uriPath?.let {
//                Glide.with(requireContext())
//                    .load(it)
//                    .placeholder(R.drawable.baseline_account_circle_24)
//                    .error(R.drawable.baseline_account_circle_24)
//                    .circleCrop() // Bo tròn ảnh
//                    .into(binding.idAvatar);
////                binding.idAvatar.setImageURI(it) // Set the image URI to the ImageView
//            }
//            viewModel.setUp(idUser,imagePath, null, null, null, null,null)
//        }
//    }
private fun navigateToFragment(fragmentId: Int) {
    val navController = findNavController()
    val currentDestination = navController.currentDestination
    if (currentDestination == null || currentDestination.id != fragmentId) {
        navController.navigate(fragmentId, null, navOptions)
    }
}
}
