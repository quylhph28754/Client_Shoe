package com.fpoly.shoes_app.framework.presentation.common

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.ViewModelActivity
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import com.fpoly.shoes_app.utility.dialog.ProgressbarDialogFragment
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private val viewModelClass: Class<VM>
) : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding as VB

    protected val viewModelActivity: ViewModelActivity by activityViewModels()

    protected val viewModel: VM by lazy {
        ViewModelProvider(this)[viewModelClass]
    }

    private var _navController: NavController? = null

    protected val navController: NavController? get() = _navController

    @Inject
    internal lateinit var sharedPreferences: SharedPreferencesManager

    @Inject
    internal lateinit var progressDialog: ProgressbarDialogFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.v(TAG, "onAttach: $this")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate: $this")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView: $this")
        _navController = requireActivity().findNavController(R.id.nav_host_fragment_content_main)
        _binding = bindingInflater.invoke(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, "onViewCreated: $this")
        (requireActivity() as MainActivity).showBottomNavigation(false)
        setupViews()
        setOnClick()
        bindViewModel()

    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "onStart: $this")
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "onResume: $this")
    }

    override fun onPause() {
        super.onPause()
        Log.v(TAG, "onPause: $this")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop: $this")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.v(TAG, "onDestroyView: $this")
        _binding = null
        _navController = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy: $this")
    }

    override fun onDetach() {
        super.onDetach()
        Log.v(TAG, "onDetach: $this")
    }
    abstract fun setupViews()

    abstract fun bindViewModel()

    abstract fun setOnClick()

    protected fun showProgressbar(isShowProgressbar: Boolean) {
        if (isShowProgressbar) progressDialog.show(childFragmentManager, null)
        else progressDialog.dismiss()
    }
}