package com.android.orderapp.ui.fragments.auth.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.orderapp.R
import com.android.orderapp.databinding.FragmentSignUpBinding
import com.android.orderapp.ui.base.BaseFragment
import com.android.orderapp.ui.base.FragmentInflate
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment : BaseFragment<SignUpViewModel, FragmentSignUpBinding>() {

    override val viewModel: SignUpViewModel by viewModels()
    override val viewBindingInflater: FragmentInflate<FragmentSignUpBinding>?
        get() = FragmentSignUpBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignup.setOnClickListener {
            viewModel.signUp(
                email = binding.signupUsername.text.toString(),
                password = binding.signupPassword.text.toString(),
                confirmPassword = binding.signupConfirmPassword.text.toString(),
                loginSuccess = {
                    findNavController().popBackStack()
                },
                loginError = { error ->
                    showToastMessage(error)
                }
            ) }

        binding.btnSignupToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}

