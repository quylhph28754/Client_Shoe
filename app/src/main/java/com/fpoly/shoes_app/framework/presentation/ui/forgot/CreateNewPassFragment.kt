package com.fpoly.shoes_app.framework.presentation.ui.forgot

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentCreateNewPassBinding

class CreateNewPassFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: FragmentCreateNewPassBinding? = null
    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNewPassBinding.inflate(inflater, container, false)
        binding.btnNextPager.setOnClickListener {
            val dialog = CustomDialogFragment()
            dialog.show(childFragmentManager, "CustomDialogFragment")
            Handler(Looper.getMainLooper()).postDelayed({
                Navigation.findNavController(requireView()).navigate(
                    R.id.profileFragment,
                    null,
                    navOptions
                )
            }, 2000L)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
