package com.fpoly.shoes_app.framework.presentation.ui.profile.editProfile

import android.app.DatePickerDialog
import android.text.Editable
import android.text.TextWatcher
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
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentEditProfileBinding
import com.fpoly.shoes_app.framework.data.module.CheckValidate
import com.fpoly.shoes_app.framework.domain.model.profile.ProfileResponse
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.setUpAccount.SetUpAccountViewModel
import com.fpoly.shoes_app.utility.Status
import com.fpoly.shoes_app.utility.service.ServiceUtil
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, SetUpAccountViewModel>(
    FragmentEditProfileBinding::inflate, SetUpAccountViewModel::class.java
) {
    private val gender = arrayOf("Nữ", "Nam")
    private var id = ""
    private var type = 1

    private var originalFullName: String? = null
    private var originalPhoneNumber: String? = null
    private var originalGmail: String? = null
    private var originalBirthDay: String? = null
    private var originalGender: Int = 0

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
        // Any additional setup before views are initialized can go here
    }

    override fun setupViews() {
        id = sharedPreferences.getIdUser()
        viewModel.profilefind(id)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, gender)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        // Add TextWatchers to track changes
        addTextWatchers()
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
                            fragmentManager?.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            ServiceUtil.playNotificationSound(requireContext(), getString(R.string.app_name), getString(R.string.updateInfor))
                            navController.navigate(
                                R.id.profileFragment,
                                null,
                                NavOptions.Builder().setPopUpTo(
                                    navController.currentDestination?.id ?: -1, true
                                ).build()
                            )
                            StyleableToast.makeText(
                                requireContext(),
                                getString(R.string.success),
                                R.style.success
                            ).show()
                        }
                    }
                    Status.ERROR -> {
                        val errorMessage = result.message ?: "Unknown error"
                        Log.e("EditProfileFragment", errorMessage)
                        showProgressbar(false)
                    }
                    Status.LOADING -> {
                        showProgressbar(true)
                    }
                    Status.INIT -> {}
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
            originalFullName = user.fullName
            originalPhoneNumber = user.phoneNumber
            originalGmail = user.gmail
            originalBirthDay = user.birthday
            originalGender = if (user.grender == "Female") 0 else 1

            binding.nameEditText.setText(originalFullName ?: getString(R.string.name))
            binding.phoneEditText.setText(originalPhoneNumber ?: getString(R.string.phone_suggest))
            binding.dateEditText.setText(originalBirthDay ?: getString(R.string.birthDay))
            binding.mailEditText.setText(originalGmail ?: getString(R.string.email))
            binding.spinner.setSelection(originalGender)
        }
    }

    private fun handleError(errorMessage: String?) {
        showProgressbar(false)
        Log.e("EditProfileFragment", "Profile error: $errorMessage")
    }

    private fun handleLoading() {
        showProgressbar(true)
    }

    private fun addTextWatchers() {
        binding.nameEditText.addTextChangedListener(createTextWatcher())
        binding.phoneEditText.addTextChangedListener(createTextWatcher())
        binding.mailEditText.addTextChangedListener(createTextWatcher())
        binding.dateEditText.addTextChangedListener(createTextWatcher())
    }

    private fun createTextWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            checkIfAnyFieldChanged()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun checkIfAnyFieldChanged() {
        val currentFullName = binding.nameEditText.text.toString().trim()
        val currentPhoneNumber = binding.phoneEditText.text.toString().trim()
        val currentGmail = binding.mailEditText.text.toString().trim()
        val currentBirthDay = binding.dateEditText.text.toString().trim()
        val currentGender = binding.spinner.selectedItemPosition

        binding.btnNextPager.isEnabled = (currentFullName != originalFullName ||
                currentPhoneNumber != originalPhoneNumber ||
                currentGmail != originalGmail ||
                currentBirthDay != originalBirthDay ||
                currentGender != originalGender)
    }

    override fun setOnClick() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = position
                checkIfAnyFieldChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
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
            val fullName = binding.nameEditText.text.toString().trim()
            val phoneNumber = binding.phoneEditText.text.toString().trim()
            val gmail = binding.mailEditText.text.toString().trim()
            val birthDay = binding.dateEditText.text.toString().trim()
            val grender = type.toString()

            binding.btnNextPager.isEnabled = false
            viewModel.setUp(
                id, null, phoneNumber, fullName, gmail, birthDay, grender
            )
        }
    }
}
