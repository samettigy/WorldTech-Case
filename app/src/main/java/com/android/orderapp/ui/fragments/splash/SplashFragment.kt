package com.android.orderapp.ui.fragments.splash


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.orderapp.R
import com.android.orderapp.databinding.FragmentSplashBinding
import com.android.orderapp.ui.base.BaseFragment
import com.android.orderapp.ui.base.FragmentInflate
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {

    override val viewModel: SplashViewModel by viewModels()
    override val viewBindingInflater: FragmentInflate<FragmentSplashBinding>
        get() = FragmentSplashBinding::inflate
    private lateinit var firebaseAuth: FirebaseAuth



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            if (firebaseAuth.currentUser != null) {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }, 2000)
    }

}