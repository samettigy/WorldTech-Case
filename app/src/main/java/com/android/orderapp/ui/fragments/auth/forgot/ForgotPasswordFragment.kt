package com.android.orderapp.ui.fragments.auth.forgot

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.orderapp.databinding.FragmentForgotPasswordBinding
import com.android.orderapp.ui.base.BaseFragment
import com.android.orderapp.ui.base.FragmentInflate
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment :
    BaseFragment<ForgotPasswordViewModel, FragmentForgotPasswordBinding>() {

    override val viewModel: ForgotPasswordViewModel by viewModels()
    override val viewBindingInflater: FragmentInflate<FragmentForgotPasswordBinding>?
        get() = FragmentForgotPasswordBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnReset.setOnClickListener {
            viewModel.forgotPassword(
                email = binding.forgotUsername.text.toString(),
                success = {
                    findNavController().popBackStack()
                },
                error = { message ->
                    showToastMessage(message)
                })
        }

        binding.btnForgotToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}