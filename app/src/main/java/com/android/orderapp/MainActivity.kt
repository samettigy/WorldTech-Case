package com.android.orderapp

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.android.orderapp.databinding.ActivityMainBinding
import com.android.orderapp.ui.base.ActivityInflate
import com.android.orderapp.ui.base.BaseActivity
import com.android.orderapp.ui.fragments.splash.SplashFragmentDirections
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val viewModel: MainViewModel by viewModels()
    override val viewBindingInflater: ActivityInflate<ActivityMainBinding>
        get() = ActivityMainBinding::inflate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createBottomNavigation()
       // checkboxChange()

    }

    fun topla(a:Int,b:String){

    }

    fun topla(a: Int,b:Int){

    }

    /*
    private fun checkboxChange() {

        var cbFav = findViewById<CheckBox>(R.id.cbFav)

        cbFav.setOnCheckedChangeListener { checkbox, isChecked ->

            if (isChecked) {
                showToast("Item added to Favorites")
            } else {
                showToast("Item removed from Favorites")
            }

        }

    }

     */


    private fun createBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView5) as NavHostFragment

        NavigationUI.setupWithNavController(binding.btmNavigation, navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val showBottomNav = when (destination.id) {
                R.id.loginFragment -> false
                R.id.signUpFragment -> false
                R.id.forgotPasswordFragment -> false
                R.id.splashFragment -> false
                R.id.editProfileFragment -> false
                R.id.detailFragment -> false
                R.id.libraryFragment -> false
                else -> true
            }
            binding.btmNavigation.visibility = if (showBottomNav) View.VISIBLE else View.GONE
        }
    }

    /*
    private fun showToast(str: String) {
        Toast.makeText(this, "str", Toast.LENGTH_SHORT)
    }

     */


}