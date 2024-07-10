package com.android.orderapp.ui.fragments.auth.login

import androidx.lifecycle.viewModelScope
import com.android.orderapp.ui.base.BaseViewModel
import com.android.orderapp.ui.base.LoadingState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : BaseViewModel() {

    fun singIn(
        email: String,
        password: String,
        loginSuccess: () -> Unit,
        loginError: (String) -> Unit
    ) = viewModelScope.launch {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            loginError.invoke("fields cannot be empty")
            return@launch
        }


        loadingState.emit(LoadingState.SHOW)
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            viewModelScope.launch {
                loadingState.emit(LoadingState.HIDE)
            }
            if (it.isSuccessful) {
                loginSuccess.invoke()
            } else {
                loginError.invoke(it.exception?.message.orEmpty())
            }
        }
    }
}