package com.fpoly.shoes_app.framework.presentation.ui.login

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentLoginScreenBinding
import com.fpoly.shoes_app.framework.domain.model.login.LoginResponse
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.Status
import com.fpoly.shoes_app.utility.toMD5
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.delay
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
    override fun setupPreViews() {

    }
    private fun setupListeners() {
        Log.e("user", sharedPreferences.getUserName()+sharedPreferences.getPassWord())

        if (sharedPreferences.getUserName().isNotEmpty() && sharedPreferences.getPassWord()
                .isNotEmpty()
        ) {
            check = true
            viewModel.signIn(
                sharedPreferences.getUserName(), sharedPreferences.getPassWord()
            )
        }
        Log.e("userWait", sharedPreferences.getUserNameWait())
        if (sharedPreferences.getUserNameWait().isNotEmpty()) binding.userNameEditTextLogin.setText(
            sharedPreferences.getUserNameWait()
        )
    }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> handleSuccess(result.data)
                    Status.ERROR -> handleError(result.message)
                    Status.LOADING -> handleLoading()
                    Status.INIT -> binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private suspend fun handleSuccess(loginResponse: LoginResponse?) {
        enableInputs()
        binding.progressBar.visibility = View.GONE
        if (loginResponse?.success == true) {
            loginResponse.user?.let { sharePre(it.id.toString()) }
            navigateToHome()
            clearInputs()
            StyleableToast.makeText(
                requireContext(), getString(R.string.success), R.style.success
            ).show()
        } else {
            showErrorMessage(loginResponse?.message)
        }
    }

    private suspend fun handleError(errorMessage: String?) {
        binding.progressBar.visibility = View.GONE
        showErrorMessage(errorMessage)
        enableInputs()

        Log.e("LoginFragment", "Login error: $errorMessage")
    }

    private fun handleLoading() {
        disableInputs()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun sharePre(id: String) {
        if ( username
                .isNotEmpty() && password.isNotEmpty()){
            sharedPreferences.setIdUser(id)
            if (check ) {
            sharedPreferences.setPassWord(username, password.toMD5())
            return
        }
            sharedPreferences.setPassWord(username,null)
        }
    }

    private fun enableInputs() {
        binding.layoutInputUserNameLogin.isEnabled = true
        binding.layoutInputPasswordLogin.isEnabled = true
        binding.switchLogin.isEnabled = true
        binding.textForGot.isEnabled = true
        binding.textSignUp.isEnabled = true
        binding.btnLogin.isEnabled = true
    }

    private fun disableInputs() {
        binding.userNameEditTextLogin.isEnabled = false
        binding.switchLogin.isEnabled = false
        binding.textForGot.isEnabled = false
        binding.textSignUp.isEnabled = false
        binding.btnLogin.isEnabled = false
        binding.passwordEditTextLogin.isEnabled = false
    }

    private fun navigateToHome() {
        val navController = findNavController()
        val navOptions = NavOptions.Builder().setPopUpTo(
            navController.currentDestination?.id ?: -1, true
        ).build()
        navController.navigate(R.id.profileFragment, null, navOptions)
    }

    private fun clearInputs() {
        binding.userNameEditTextLogin.text?.clear()
        binding.passwordEditTextLogin.text?.clear()
    }

    private suspend fun showErrorMessage(errorMessage: String?) {
        StyleableToast.makeText(
            requireContext(), getString(R.string.fail_password), R.style.fail
        ).show()
        binding.layoutInputUserNameLogin.error = errorMessage
        binding.layoutInputPasswordLogin.error = errorMessage
        delay(2000)
        binding.layoutInputUserNameLogin.error =null
        binding.layoutInputPasswordLogin.error =null
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
