package com.fpoly.shoes_app.framework.presentation.ui.setUpAccount

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentSetUpAccountBinding
import com.fpoly.shoes_app.framework.data.othetasks.AddImage
import com.fpoly.shoes_app.framework.data.module.CheckValidate
import com.fpoly.shoes_app.framework.data.othetasks.AddImage.Glides
import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccount
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.Imagesss
import com.fpoly.shoes_app.utility.Status
import com.fpoly.shoes_app.utility.getBitmapFromDrawable
import com.fpoly.shoes_app.utility.service.ServiceUtil.playNotificationSound
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar

@AndroidEntryPoint
class SetUpAccountFragment : BaseFragment<FragmentSetUpAccountBinding, SetUpAccountViewModel>(
    FragmentSetUpAccountBinding::inflate, SetUpAccountViewModel::class.java
) {
    private lateinit var captureImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private val gender = arrayOf("Nữ", "Nam")
    private var uriPath :Uri?=null
    private var id: String = ""
    private var type: Int = 1

    private fun showDatePickerDialog(dateEditText: EditText, layoutData: TextInputLayout) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, yearBirth, monthBirth, dayOfMonth ->
            val selectedDate = "$yearBirth-${monthBirth + 1}-$dayOfMonth"
            dateEditText.setText(selectedDate)
            layoutData.requestFocus()
        }, year, month, day)

        datePickerDialog.show()
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
    private fun handleResult(uri: Uri?) {
        uri?.let {
            Glides(it,requireContext(),binding.imgAvatar)
        }
    }
    override fun setupViews() {
        id = arguments?.getString("id") ?: sharedPreferences.getIdUser()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, gender)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
    }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setUpResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        showProgressbar(false)
                        result.data?.let { signUpResponse ->
                            if (signUpResponse.success) {
                                val navController = findNavController()
                                binding.nameEditText.text?.clear()
                                binding.mailEditText.text?.clear()
                                binding.phoneEditText.text?.clear()
                                playNotificationSound(requireContext(),"Thiết lập tài khoản thành công","")
                                if (sharedPreferences.getIdUser().isNullOrEmpty()){
                                fragmentManager?.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                navController.navigate(
                                    R.id.loginFragmentScreen, null, NavOptions.Builder().setPopUpTo(
                                        navController.currentDestination?.id ?: -1, true
                                    ).build()

                                )}else{
                                    findNavController().popBackStack()
                                }
                                StyleableToast.makeText(
                                    requireContext(), getString(R.string.success), R.style.success
                                ).show()
                                return@collect
                            }
                        }
                    }

                    Status.ERROR -> {
                        val errorMessage = result.message ?: "Unknown error"
                        Log.e("error", errorMessage)
//                        showProgressbar(false)
                    }

                    Status.LOADING -> {
                        showProgressbar(true)

                    }
//                        showProgressbar(true)
                    Status.INIT -> { /* No-op */ }
                }
            }
        }
    }

    override fun setOnClick() {
        binding.relative.setOnClickListener {
            val bmp = requireContext().getBitmapFromDrawable(R.drawable.baseline_account_circle_24)
            AddImage.openImageDialog(null,requireContext(), requireActivity()) { intent ->
                when (intent.action) {
                    MediaStore.ACTION_IMAGE_CAPTURE -> captureImageLauncher.launch(intent)
                    Intent.ACTION_PICK -> pickImageLauncher.launch(intent)
                }
            }        }

        binding.dateEditText.setOnClickListener {
            showDatePickerDialog(binding.dateEditText, binding.layoutInputDate)
        }
        binding.toolbar.setNavigationOnClickListener {
            if (sharedPreferences.getIdUser().isNullOrEmpty()){
                fragmentManager?.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            navController!!.navigate(
                R.id.loginFragmentScreen, null, NavOptions.Builder().setPopUpTo(
                    navController!!.currentDestination?.id ?: -1, true
                ).build())
                return@setNavigationOnClickListener
            }
                      findNavController().popBackStack()
        }
        binding.btnNextPager.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val mail = binding.mailEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            val birth = binding.dateEditText.text.toString().trim()
            val gender = binding.spinner.selectedItem.toString()
            val imagePath = uriPath?.let { File(Imagesss.getPathFromUri(it, requireContext())) }

                viewModel.setUp(id,imagePath,phone,name,mail,birth, gender )

        }
    }
}