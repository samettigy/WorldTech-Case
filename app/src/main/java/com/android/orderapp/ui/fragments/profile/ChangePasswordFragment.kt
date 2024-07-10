package com.android.orderapp.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.android.orderapp.R
import com.android.orderapp.databinding.FragmentChangePasswordBinding
import com.android.orderapp.ui.base.BaseFragment
import com.android.orderapp.ui.base.FragmentInflate
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ChangePasswordFragment :
    BaseFragment<ChangePasswordViewModel, FragmentChangePasswordBinding>() {

    private lateinit var firebaseAuth: FirebaseAuth
    override val viewModel: ChangePasswordViewModel by viewModels()
    override val viewBindingInflater: FragmentInflate<FragmentChangePasswordBinding>
        get() = FragmentChangePasswordBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = Firebase.auth

        binding.btnUpdate.setOnClickListener {

            val user = firebaseAuth.currentUser
            val password = binding.updatePasswordText.text.toString()
            val confirmPass = binding.updateConfirmPasswordText.text.toString()
            user?.updatePassword(password)?.addOnCompleteListener {

                if (confirmPass != password) {
                    showToastMessage("Passwords do not match")
                }

                if (it.isSuccessful) {
                    showToastMessage("Successful")
                } else {
                    showToastMessage("Error try again")
                }

            }

        }

    }



}