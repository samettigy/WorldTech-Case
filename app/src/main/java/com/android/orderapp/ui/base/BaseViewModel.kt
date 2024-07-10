package com.android.orderapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    var loadingState: MutableSharedFlow<LoadingState> = MutableSharedFlow<LoadingState>()

    protected fun showLoadingDialog() = viewModelScope.launch{
        loadingState.emit(LoadingState.SHOW)
    }

    protected fun hideLoadingDialog() = viewModelScope.launch{
        loadingState.emit(LoadingState.HIDE)
    }
}

enum class LoadingState {
    SHOW,
    HIDE
}