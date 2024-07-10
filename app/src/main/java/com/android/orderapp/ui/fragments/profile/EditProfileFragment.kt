package com.android.orderapp.ui.fragments.profile

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.orderapp.R
import com.android.orderapp.databinding.FragmentEditProfileBinding
import com.android.orderapp.ui.base.BaseFragment
import com.android.orderapp.ui.base.FragmentInflate
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<EditProfileViewModel, FragmentEditProfileBinding>() {


    override val viewModel: EditProfileViewModel by viewModels()
    override val viewBindingInflater: FragmentInflate<FragmentEditProfileBinding>
        get() = FragmentEditProfileBinding::inflate
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.selectedImg = uri
                binding.profileImg.setImageURI(viewModel.selectedImg)
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is EditProfileScreenState.Content -> {
                    binding.loadingView.visibility = View.GONE
                    binding.errorView.visibility = View.GONE
                    binding.contentView.visibility = View.VISIBLE
                    binding.profileName.setText(state.user.name)
                    binding.profileSurname.setText(state.user.surname)
                    binding.profileGender.setText(state.user.gender)
                    binding.profileEmail.setText(state.user.email)
                    if (viewModel.selectedImg != null) {
                        binding.profileImg.setImageURI(viewModel.selectedImg)
                    } else {
                        context?.let {
                            Glide.with(it)
                                .load(state.user.profileImageUrl)
                                .placeholder(R.drawable.ic_pp)
                                .into(binding.profileImg)
                        }
                    }
                }

                is EditProfileScreenState.Error -> {
                    showToastMessage(state.error)
                    findNavController().popBackStack()
                }

                EditProfileScreenState.Loading -> {
                    binding.contentView.visibility = View.GONE
                    binding.errorView.visibility = View.GONE
                    binding.loadingView.visibility = View.VISIBLE
                }
            }
        }


        binding.profileImg.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        binding.updateButton.setOnClickListener {
            //show loading
            val edittextName = binding.profileName.text.toString()
            val edittextSurname = binding.profileSurname.text.toString()
            val edittextGender = binding.profileGender.text.toString()
            viewModel.updateProfile(
                edittextName = edittextName,
                edittextSurname = edittextSurname,
                edittextGender = edittextGender,
                onError = {
                    //hideloading
                    showToastMessage(it)
                },
                onSuccess = {
//hideloading
                    findNavController().popBackStack()

                },
            )
            //todo user bilgileri methoda paslanacak
        }
    }
}





