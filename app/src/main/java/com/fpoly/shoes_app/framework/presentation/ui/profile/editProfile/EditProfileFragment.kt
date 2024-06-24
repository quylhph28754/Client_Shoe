package com.fpoly.shoes_app.framework.presentation.ui.profile.editProfile

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.databinding.FragmentEditProfileBinding
import com.fpoly.shoes_app.framework.data.module.CheckValidate
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val gender = arrayOf("Nam","Nữ")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }
    return  binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, gender)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.getItemAtPosition(position) as String
                // Xử lý khi một quốc gia được chọn
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Xử lý khi không có mục nào được chọn
            }
        }
        binding.mailEditText.setOnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                CheckValidate.checkEmail(requireContext(), binding.mailEditText, binding.layoutInputMail,binding.layoutInputPhone)
                return@setOnEditorActionListener true
            }
            false
        }
        binding.phoneEditText.setOnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                CheckValidate.checkPhone(requireContext(), binding.phoneEditText, binding.layoutInputPhone,binding.btnNextPager)
                return@setOnEditorActionListener true
            }
            false
        }
        binding.dateEditText.setOnClickListener {
            showDatePickerDialog(binding.dateEditText,binding.layoutInputMail)
        }
    }
    private fun showDatePickerDialog(dateEditText: EditText, layoutData: TextInputLayout) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            // Xử lý khi người dùng chọn ngày tháng
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            dateEditText.setText(selectedDate)
            layoutData.requestFocus()

        }, year, month, day)

        datePickerDialog.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}