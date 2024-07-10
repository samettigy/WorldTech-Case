package com.android.orderapp.ui.fragments.profile

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.orderapp.data.model.UserInfo
import com.android.orderapp.data.repository.MoviesRepository
import com.android.orderapp.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val fireStore: FirebaseFirestore,
    private val moviesRepository: MoviesRepository
) : BaseViewModel() {

    private val _screenState =
        MutableLiveData<EditProfileScreenState>(EditProfileScreenState.Loading)
    val screenState: LiveData<EditProfileScreenState> = _screenState
    private val docRef = fireStore.collection("users").document("${firebaseAuth.uid}")
    var selectedImg: Uri? = null
    var user : UserInfo? = null

    init {
        getUser()
    }

    private fun getUser() {
        _screenState.value = EditProfileScreenState.Loading
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                _screenState.value = document.toObject(UserInfo::class.java)?.let {
                    user = it
                    EditProfileScreenState.Content(it)
                }
            } else {
                _screenState.value = EditProfileScreenState.Error("User bulunamadi")
            }
        }.addOnFailureListener() {
            _screenState.value = EditProfileScreenState.Error("${it.message}")
        }
    }

    fun updateProfile(
        edittextName: String, edittextSurname: String, edittextGender: String,
        onSuccess: () -> Unit, onError: (message: String) -> Unit
    ) {
        if (edittextName.isEmpty() || edittextSurname.isEmpty() || edittextGender.isEmpty()) {
            onError.invoke("fields cannot be empty")
        } else {
            uploadImageToFirebaseStorage(
                edittextName,
                edittextSurname,
                edittextGender,
                onSuccess,
                onError
            )
        }
    }

    private fun uploadImageToFirebaseStorage(
        edittextName: String,
        edittextSurname: String,
        edittextGender: String, onSuccess: () -> Unit, onError: (message: String) -> Unit
    ) {
        showLoadingDialog()
        if (selectedImg == null) {
            saveUser(
                urlPhoto = user?.profileImageUrl,
                edittextName,
                edittextSurname,
                edittextGender,
                onSuccess,
                onError
            )
            return
        }

        val ref = FirebaseStorage.getInstance().getReference()
            .child("images").child(firebaseAuth.uid ?: "")// todo user id kullanilacak

        ref.putFile(selectedImg!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    saveUser(
                        uri.toString(),
                        edittextName,
                        edittextSurname,
                        edittextGender,
                        onSuccess,
                        onError
                    )
                }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Dosya yüklenirken bir hata oluştu")
                hideLoadingDialog()
            }
    }

    private fun saveUser(
        urlPhoto: String? = null,
        edittextName: String,
        edittextSurname: String,
        edittextGender: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) { // todo kullanici bilgileri ve fotograf download url paslanaca

        val newData = hashMapOf<String, Any>(
            "name" to edittextName,
            "surname" to edittextSurname,
            "gender" to edittextGender,
            "profileImageUrl" to (urlPhoto.orEmpty())
        )

        docRef.update(newData)
            .addOnSuccessListener {
                Log.d(
                    ContentValues.TAG,
                    "Belge başarıyla güncellendi!"
                )
                hideLoadingDialog()
                onSuccess()
            }
            .addOnFailureListener { e ->
                hideLoadingDialog()
                Log.w(ContentValues.TAG, "Hata oluştu: ", e)
                onError(e.message ?: "Bir hata oluştu")
            }


    }
}

sealed class EditProfileScreenState() {
    data object Loading : EditProfileScreenState()
    class Error(val error: String) : EditProfileScreenState()
    class Content(val user: UserInfo) : EditProfileScreenState()
}

