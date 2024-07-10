package com.android.orderapp.ui.fragments.auth.register

import androidx.lifecycle.viewModelScope
import com.android.orderapp.data.model.UserInfo
import com.android.orderapp.ui.base.BaseViewModel
import com.android.orderapp.ui.base.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : BaseViewModel() {


    fun signUp(
        email: String,
        password: String,
        confirmPassword: String,
        loginSuccess: () -> Unit,
        loginError: (String) -> Unit
    ) = viewModelScope.launch {

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            loginError.invoke("Fields cannot be empty")
            return@launch
        }

        loadingState.emit(LoadingState.SHOW)
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            viewModelScope.launch {
                loadingState.emit(LoadingState.HIDE)
            }
            if (it.isSuccessful) {
                loginSuccess.invoke()
                val user = it.result.user
                val userId = user!!.uid
                val userEmail = user.email

                val db = Firebase.firestore

                val userDocument = db.collection("users").document(userId)

                val userInfo = UserInfo(
                    email = userEmail
                )

                userDocument.set(userInfo)
            } else {
                loginError.invoke(it.exception?.message.orEmpty())
            }
        }
    }

}