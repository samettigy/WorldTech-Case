package com.android.orderapp.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.orderapp.data.model.UserInfo
import com.android.orderapp.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : BaseViewModel() {

    private val _user = MutableLiveData<UserInfo?>(null)
    val user: LiveData<UserInfo?> = _user
    private val docRef = firebaseFirestore.collection("users").document("${firebaseAuth.uid}")

    fun getUser() {
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                _user.value = document.toObject(UserInfo::class.java)
            }
        }
    }




}