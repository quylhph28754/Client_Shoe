package com.fpoly.shoes_app.framework.presentation.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentLoginScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragmentScreen : Fragment() {

    private var _binding: FragmentLoginScreenBinding? = null
    private val binding get() = _binding!!
    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left) // Optional: You may need to define this animation as well
        .setPopExitAnim(R.anim.slide_out_right) // Optional: You may need to define this animation as well
        .build()
    private val navOptions1 = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_left)
        .setExitAnim(R.anim.slide_out_right)
        .setPopEnterAnim(R.anim.slide_in_right)
        .setPopExitAnim(R.anim.slide_out_left)
       .build()
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        binding.textSignUp.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.signUpFragment, null, navOptions)
        }
        binding.btnLogin.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.profileFragment, null, navOptions)

        }
        binding.textForGot.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.forGotFragment, null, navOptions1)

        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}