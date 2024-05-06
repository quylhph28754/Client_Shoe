package com.fpoly.shoes_app.framework.presentation.ui.forgot

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentForGotBinding


class ForGotFragment : Fragment() {
    private var _binding: FragmentForGotBinding? = null
    private val binding get() = _binding!!
    private var isPhoneMode = true
    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left) // Optional: You may need to define this animation as well
        .setPopExitAnim(R.anim.slide_out_right) // Optional: You may need to define this animation as well
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentForGotBinding.inflate(inflater, container, false)
        binding.phoneEditText.hint = getString(R.string.phoneSetUpAcc)

        binding.phoneEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEndWidth =
                    binding.phoneEditText.compoundDrawablesRelative[2]?.bounds?.width() ?: 0
                if (event.rawX >= (binding.phoneEditText.right - drawableEndWidth)) {
                    toggleMode()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
        binding.btnNextPager.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.OPTFragment, null, navOptions)

        }
        return binding.root
    }

    private fun toggleMode() {
        isPhoneMode = !isPhoneMode
        if (isPhoneMode) {
            // Chế độ điện thoại
            binding.phoneEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.baseline_local_phone_24,
                0,
                R.drawable.baseline_change_circle_24,
                0
            )
            binding.phoneEditText.hint = getString(R.string.phoneSetUpAcc)
            binding.phoneEditText.inputType = InputType.TYPE_CLASS_PHONE
        } else {
            // Chế độ email
            binding.phoneEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.baseline_attach_email_24,
                0,
                R.drawable.baseline_change_circle_24,
                0
            )
            binding.phoneEditText.hint = getString(R.string.emailSetUpAcc)
            binding.phoneEditText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}