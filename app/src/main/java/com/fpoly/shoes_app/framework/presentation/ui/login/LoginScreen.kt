package com.fpoly.shoes_app.framework.presentation.ui.login

import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentLoginScreenBinding
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.Status
import com.fpoly.shoes_app.utility.toMD5
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginScreen : BaseFragment<FragmentLoginScreenBinding, LoginViewModel>(
    FragmentLoginScreenBinding::inflate, LoginViewModel::class.java
) {
    private var check = false
    private var username: String = ""
    private var password: String = ""
    private val navOptions =
        NavOptions.Builder().setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left).setPopExitAnim(R.anim.slide_out_right).build()

    private val navOptions1 =
        NavOptions.Builder().setEnterAnim(R.anim.slide_in_left).setExitAnim(R.anim.slide_out_right)
            .setPopEnterAnim(R.anim.slide_in_right).setPopExitAnim(R.anim.slide_out_left).build()




    private fun setupListeners() {
        if (sharedPreferences.getUserName()
                .isNotEmpty() && sharedPreferences.getPassWord().isNotEmpty()
        ) {
            check = true
            viewModel.signIn(
                sharedPreferences.getUserName(), sharedPreferences.getPassWord()
            )
            return
        }
        Log.e("user",sharedPreferences.getUserName())
        binding.userNameEditTextLogin.setText(sharedPreferences.getUserName())

    }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        binding.layoutInputUserNameLogin.isEnabled = true
                        binding.layoutInputPasswordLogin.isEnabled = true
                        binding.switchLogin.isEnabled = true
                        binding.textForGot.isEnabled = true
                        binding.textSignUp.isEnabled = true
                        binding.btnLogin.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                        val loginResponse = result.data
                        if (loginResponse?.success == true) {
                            val navController = findNavController()

                            if (check) {
                                sharedPreferences.setPassWord(username, password.toMD5())
                                fragmentManager?.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                navController.navigate(
                                    R.id.homeFragment, null, NavOptions.Builder().setPopUpTo(
                                            navController.currentDestination?.id ?: -1, true
                                        ).build()
                                )
                                binding.userNameEditTextLogin.text?.clear()
                                binding.passwordEditTextLogin.text?.clear()
                                StyleableToast.makeText(
                                    requireContext(), getString(R.string.success), R.style.success
                                ).show()
                                return@collect
                            }
                            sharedPreferences.setPassWord(username, null)
                            fragmentManager?.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            navController.navigate(
                                R.id.homeFragment,
                                null,
                                NavOptions.Builder()
                                    .setPopUpTo(navController.currentDestination?.id ?: -1, true)
                                    .build()
                            )
                            binding.passwordEditTextLogin.text?.clear()
                            return@collect
                        }

                        loginResponse?.message?.let { errorMessage ->
                            StyleableToast.makeText(
                                requireContext(), getString(R.string.fail_password), R.style.fail
                            ).show()
                            binding.layoutInputUserNameLogin.error = errorMessage
                            binding.layoutInputPasswordLogin.error = errorMessage
                        }

                    }

                    Status.ERROR -> {
                        binding.layoutInputUserNameLogin.isEnabled = true
                        binding.layoutInputPasswordLogin.isEnabled = true
                        binding.switchLogin.isEnabled = true
                        binding.textForGot.isEnabled = true
                        binding.textSignUp.isEnabled = true
                        binding.btnLogin.isEnabled = true
                        val errorMessage = result.message ?: "Unknown error"
                        Log.e("LoginFragment", "Login error: $errorMessage")
                    }

                    Status.LOADING -> {
                        binding.userNameEditTextLogin.isEnabled = false
                        binding.switchLogin.isEnabled = false
                        binding.textForGot.isEnabled = false
                        binding.textSignUp.isEnabled = false
                        binding.btnLogin.isEnabled = false
                        binding.passwordEditTextLogin.isEnabled = false
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Status.INIT -> {
                        binding.progressBar.visibility = View.GONE

                    }
                }
            }
        }
    }

    override fun setupViews() {

        setupListeners()
    }

    override fun setOnClick() {
        binding.textSignUp.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment, null, navOptions)
        }
        binding.btnLogin.setOnClickListener {
            username = binding.userNameEditTextLogin.text?.trim().toString()
            password = binding.passwordEditTextLogin.text?.trim().toString()
            viewModel.signIn(username, password.toMD5())
        }
        binding.textForGot.setOnClickListener {
            findNavController().navigate(R.id.forGotFragment, null, navOptions1)
        }
        binding.switchLogin.setOnCheckedChangeListener { _, isChecked ->
            check = isChecked
            Log.e("check", check.toString())
        }
    }
}