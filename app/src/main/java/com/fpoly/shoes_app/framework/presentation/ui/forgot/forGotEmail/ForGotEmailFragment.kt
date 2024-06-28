package com.fpoly.shoes_app.framework.presentation.ui.forgot.forGotEmail

import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentForGotBinding
import com.fpoly.shoes_app.framework.data.module.CheckValidate.strNullOrEmpty
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMail
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.SharedPreferencesManager.setIdUser
import com.fpoly.shoes_app.utility.Status
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForGotEmailFragment : BaseFragment<FragmentForGotBinding, ForGotEmailViewModel>(
    FragmentForGotBinding::inflate, ForGotEmailViewModel::class.java
) {
    override fun setupViews() {

    }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.forgotMailResult.collect {

                result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        val forgotMailResponse = result.data
                        if (forgotMailResponse?.success == true) {
                            val navController = findNavController()
                            navController.navigate(
                                R.id.OPTFragment,null, NavOptions.Builder().setPopUpTo(
                                    navController.currentDestination?.id ?: -1, true
                                ).build()
                            )
                            sharedPreferences.setIdUser(forgotMailResponse.idAccount!!)
                            StyleableToast.makeText(
                                requireContext(), getString(R.string.success), R.style.success
                            ).show()
                            return@collect
                        }

                    }

                    Status.ERROR -> {

                        val errorMessage = strNullOrEmpty(result.message)
                        StyleableToast.makeText(
                            requireContext(), strNullOrEmpty(errorMessage), R.style.fail
                        ).show()
                        binding.emailEditText.error = strNullOrEmpty(errorMessage)
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Status.INIT -> {
                        binding.progressBar.visibility = View.GONE

                    }
                }
                binding.btnNextPager.isEnabled= true
            }
        }

    }

    override fun setOnClick() {
        binding.btnNextPager.setOnClickListener {
            binding.btnNextPager.isEnabled= false
            Log.e("email",binding.emailEditText.text.toString().trim())
            viewModel.forgotMail(ForgotMail( binding.emailEditText.text.toString().trim()))
        }
    }
}