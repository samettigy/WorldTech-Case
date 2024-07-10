package com.android.orderapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

typealias ActivityInflate<T> = (LayoutInflater) -> T

abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {

    abstract val viewModel: VM

    open val viewBindingInflater: ActivityInflate<VB>? = null

    lateinit var binding: VB

    open var viewBinding: VB? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBindingInflater?.invoke(layoutInflater)?.let { nonNullViewBinding ->
            viewBinding = nonNullViewBinding
            binding = nonNullViewBinding
        }

        setContentView(binding.root)
    }
}
