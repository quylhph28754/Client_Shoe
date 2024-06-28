package com.fpoly.shoes_app.framework.presentation.ui.signUp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentLoginScreenBinding
import com.fpoly.shoes_app.databinding.FragmentSignUpBinding
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.login.LoginViewModel
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import com.fpoly.shoes_app.utility.Status
import com.fpoly.shoes_app.utility.toMD5
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpViewModel>(
    FragmentSignUpBinding::inflate, SignUpViewModel::class.java
) {

    override fun setupViews() {
    }

    @SuppressLint("SuspiciousIndentation")
    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signUpResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        val signUpResponse = result.data
                        if (signUpResponse?.success == true) {
                            var bundle = Bundle()
                                bundle.putString("id", signUpResponse.user?.id)
                            val navController = findNavController()
                            binding.userNameEditText.text?.clear()
                            binding.passwordEditText.text?.clear()
                            binding.rePasswordEditText.text?.clear()
                            navController.navigate(
                                R.id.setUpAccountFragment, bundle, NavOptions.Builder().setPopUpTo(
                                    navController.currentDestination?.id ?: -1, true
                                ).build()
                            )
                            StyleableToast.makeText(
                                requireContext(), getString(R.string.success), R.style.success
                            ).show()
                            return@collect
                        }

                        signUpResponse?.message?.let { errorMessage ->
                            StyleableToast.makeText(
                                requireContext(),
                                getString(R.string.accountAlreadyExists),
                                R.style.fail
                            ).show()
                            binding.userNameEditText.error = errorMessage

                        }

                    }

                    Status.ERROR -> {
                        val errorMessage = result.message ?: "Unknown error"
                        binding.progressBar.visibility = View.GONE
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Status.INIT -> {
                        binding.progressBar.visibility = View.GONE

                    }
                }
            }
        }
    }

    override fun setOnClick() {
        binding.textLogin.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSignUp.setOnClickListener {
            val password = binding.passwordEditText.text?.toString()?.trim()
            val rePassword = binding.rePasswordEditText.text?.toString()?.trim()

            if (!password.isNullOrEmpty() && !rePassword.isNullOrEmpty()) {
                if (password == rePassword) {
                    viewModel.signUp(binding.userNameEditText.text.toString().trim(), password.toMD5())
                } else {
                    StyleableToast.makeText(
                        requireContext(), getString(R.string.passwordIncorrect), R.style.fail
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), R.string.inputFullInfo, Toast.LENGTH_SHORT).show()
            }

        }
    }
}