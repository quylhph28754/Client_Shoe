package com.fpoly.shoes_app.framework.presentation.ui.profile.editProfile

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.databinding.FragmentEditProfileBinding
import com.fpoly.shoes_app.framework.data.module.CheckValidate
import com.fpoly.shoes_app.framework.domain.model.profile.ProfileResponse
import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccount
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.setUpAccount.SetUpAccountViewModel
import com.fpoly.shoes_app.utility.Status
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch
import java.util.Calendar

@Suppress("DEPRECATION", "UNUSED_EXPRESSION")
@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, SetUpAccountViewModel>(
    FragmentEditProfileBinding::inflate, SetUpAccountViewModel::class.java
) {
    private val gender = arrayOf("Nữ", "Nam")
    private var id = ""
    private var type = 1

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

    }

    override fun setupViews() {
        id = sharedPreferences.getIdUser()
        viewModel.profilefind(id)
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
                            val setUpResponse = result.data
                            if (setUpResponse?.success == true) {
                                val navController = findNavController()
                                binding.nameEditText.text?.clear()
                                binding.mailEditText.text?.clear()
                                binding.phoneEditText.text?.clear()
                                fragmentManager?.popBackStackImmediate(
                                    null, FragmentManager.POP_BACK_STACK_INCLUSIVE
                                )

                                navController.navigate(
                                    com.fpoly.shoes_app.R.id.profileFragment,
                                    null,
                                    NavOptions.Builder().setPopUpTo(
                                        navController.currentDestination?.id ?: -1, true
                                    ).build()
                                )
                                StyleableToast.makeText(
                                    requireContext(),
                                    getString(com.fpoly.shoes_app.R.string.success),
                                    com.fpoly.shoes_app.R.style.success
                                ).show()
                                return@collect
                            }
                        }

                        Status.ERROR -> {
                            val errorMessage = result.message ?: "Unknown error"
                            Log.e("error",errorMessage)
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
        showProgressbar(false)
        profileResponse?.user?.let { user ->
            binding.nameEditText.setText(
                user.fullName ?: getString(com.fpoly.shoes_app.R.string.name)
            )
            binding.phoneEditText.setText(
                user.phoneNumber ?: getString(com.fpoly.shoes_app.R.string.phone_suggest)
            )
            binding.dateEditText.setText(
                user.birthday ?: getString(com.fpoly.shoes_app.R.string.birthDay)
            )
            binding.mailEditText.setText(
                user.gmail ?: getString(com.fpoly.shoes_app.R.string.email)
            )
            if (user.grender == "Female") {
                binding.spinner.setSelection(0)
            } else {
                binding.spinner.setSelection(1)
            }
        }
    }

    private fun handleError(errorMessage: String?) {
        showProgressbar(false)
        Log.e("ProfileFragment", "Profile error: $errorMessage")
    }

    private fun handleLoading() {
        showProgressbar(true)
    }

    override fun setOnClick() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                parent?.getItemAtPosition(position) as String
                type = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                false
            }
        }

        binding.mailEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                CheckValidate.checkEmail(
                    requireContext(),
                    binding.mailEditText,
                    binding.layoutInputMail,
                    binding.layoutInputPhone
                )
                true
            } else {
                false
            }
        }

        binding.phoneEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                CheckValidate.checkPhone(
                    requireContext(),
                    binding.phoneEditText,
                    binding.layoutInputPhone,
                    binding.btnNextPager
                )
                true
            } else {
                false
            }
        }

        binding.nameEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                CheckValidate.checkStr(
                    requireContext(),
                    binding.phoneEditText,
                    binding.layoutInputPhone,
                    binding.layoutInputMail
                )
                true
            } else {
                false
            }
        }

        binding.dateEditText.setOnClickListener {
            showDatePickerDialog(binding.dateEditText, binding.layoutInputMail)
        }

        binding.btnNextPager.setOnClickListener {
            val fullName = binding.nameEditText
            val phoneNumber = binding.phoneEditText
            val gmail = binding.mailEditText
            val birthDay = binding.dateEditText
            val grender = type.toString()
            binding.btnNextPager.isEnabled = false
            fullName.isEnabled =false
            phoneNumber.isEnabled =false
            gmail.isEnabled =false
            birthDay.isEnabled =false
            viewModel.setUp(
                id, null,phoneNumber.text.toString().trim(), fullName.text.toString().trim(), gmail.text.toString().trim(), birthDay.text.toString().trim(), grender
            )
        }
    }
}
