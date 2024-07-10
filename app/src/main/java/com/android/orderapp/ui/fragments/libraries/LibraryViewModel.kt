package com.android.orderapp.ui.fragments.libraries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.orderapp.data.model.MovieModel
import com.android.orderapp.ui.base.BaseViewModel
import com.android.orderapp.ui.fragments.favorite.FavoritesScreenState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val gson: Gson,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : BaseViewModel() {

    private val _screenState =
        MutableLiveData<LibrariesScreenState>(LibrariesScreenState.Loading)
    val screenState: LiveData<LibrariesScreenState> = _screenState
    val currentUser = firebaseAuth.currentUser?.uid
    val docRef = firebaseFirestore.collection("libraries").document("$currentUser")

    init {
        getLibraries()
    }


    fun getLibraries() {
        _screenState.value = LibrariesScreenState.Loading
        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val items = document.get("items") as? List<String>
                items?.forEach {
                    if (items.isNotEmpty()) {
                        _screenState.value = LibrariesScreenState.Content(items.map {
                            gson.fromJson(
                                it,
                                MovieModel::class.java
                            )
                        })
                    } else {
                        _screenState.value =
                            LibrariesScreenState.Error("Your library is empty")
                    }
                } ?: run {
                    _screenState.value =
                        LibrariesScreenState.Error("Your library is empty")
                }
            } else {
                _screenState.value =
                    LibrariesScreenState.Error("Your library is empty")
            }
        }.addOnFailureListener {
            _screenState.value = LibrariesScreenState.Error("Error while loading your library")
        }
    }


}


sealed class LibrariesScreenState() {
    data object Loading : LibrariesScreenState()
    class Error(val error: String) : LibrariesScreenState()
    class Content(val movies: List<MovieModel>) : LibrariesScreenState()
}