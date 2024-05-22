package com.fpoly.shoes_app.framework.presentation.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentLoginScreenBinding
import com.fpoly.shoes_app.framework.domain.model.login.LoginResult


class LoginScreen : Fragment() {

    private var _binding: FragmentLoginScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel
    private lateinit var username :String
    private lateinit var password :String
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
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setupListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    private fun setupListeners() {
        binding.textSignUp.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.signUpFragment, null, navOptions)
        }
        binding.btnLogin.setOnClickListener {
            username = binding.userNameEditTextLogin.text?.trim().toString()
            password = binding.passwordEditTextLogin.text?.trim().toString()
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.signIn(username, password).collect { result ->
                    when (result) {
                        is LoginResult.Success -> {
                            val loginResponse = result.loginResponse
                            Log.e("success",loginResponse.user.toString())
                            if(loginResponse.success)
                                Navigation.findNavController(requireView()).navigate(R.id.homeFragment, null, navOptions1)
                            else{
                                binding.layoutInputUserNameLogin.error=loginResponse.message
                                binding.layoutInputPasswordLogin.error=loginResponse.message}
                        }
                        is LoginResult.Error -> {
                            val errorMessage = result.errorMessage
                            Log.e("faile",errorMessage.toString())
                        }
                    }
                }
            }
        }
        binding.textForGot.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.forGotFragment, null, navOptions1)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}