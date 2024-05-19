package com.fpoly.shoes_app.framework.presentation.ui.signUp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left) // Optional: You may need to define this animation as well
        .setPopExitAnim(R.anim.slide_out_right) // Optional: You may need to define this animation as well
        .build()
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.textLogin.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.setUpAccountFragment, null, navOptions)
        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}