package com.android.orderapp.ui.base

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.android.orderapp.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

typealias  FragmentInflate<T> = (inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> T

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null

    public val binding: VB get() = _binding!!

    open val viewBindingInflater: FragmentInflate<VB>? = null

    abstract val viewModel: VM

    private var dialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = viewBindingInflater?.invoke(inflater, container, false)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadingState.collect {
                    if (it == LoadingState.SHOW) {
                        showLoadingDialog()
                    } else {
                        hideLoadingDialog()
                    }
                }
            }
        }

    }

    private fun showLoadingDialog() {
        if (dialog == null) {
            val dialogView = LayoutInflater.from(activity).inflate(R.layout.loading_dialog, null)
            val builder = AlertDialog.Builder(activity)
            builder.setView(dialogView)
            dialog = builder.create()
        }
        dialog?.show()
    }

    private fun hideLoadingDialog() {
        dialog?.dismiss()
    }


    open fun showToastMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}