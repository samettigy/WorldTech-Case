package com.android.orderapp.ui.fragments.auth.login


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.android.orderapp.R
import com.android.orderapp.databinding.FragmentLoginBinding
import com.android.orderapp.ui.base.BaseFragment
import com.android.orderapp.ui.base.FragmentInflate
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()
    override val viewBindingInflater: FragmentInflate<FragmentLoginBinding>
        get() = FragmentLoginBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            viewModel.singIn(
                email = binding.textLoginUsername.text.toString(),
                password = binding.textLoginPassword.text.toString(),
                loginSuccess = {
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }, 1000)
                },
                loginError = { message ->
                    showToastMessage(message)
                })
        }



        binding.textForgot.setOnClickListener {
            findNavController().navigate(R.id.loginToForgotPassword)
        }

        binding.textSignUp.setOnClickListener {
            findNavController().navigate(R.id.loginToSignup)
        }
    }


}