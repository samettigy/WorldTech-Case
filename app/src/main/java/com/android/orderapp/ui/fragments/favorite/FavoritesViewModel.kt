package com.android.orderapp.ui.fragments.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.android.orderapp.data.adapter.MovieAdapter
import com.android.orderapp.data.adapter.MovieAdapterInteraction
import com.android.orderapp.data.model.FavInfo
import com.android.orderapp.data.model.MovieModel
import com.android.orderapp.data.model.UserInfo
import com.android.orderapp.ui.base.BaseViewModel
import com.android.orderapp.ui.fragments.profile.EditProfileScreenState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val gson: Gson,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : BaseViewModel() {


    private val _screenState =
        MutableLiveData<FavoritesScreenState>(FavoritesScreenState.Loading)
    val screenState: LiveData<FavoritesScreenState> = _screenState
    val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    val docRef = FirebaseFirestore.getInstance().collection("favorites").document("$currentUser")

    init {
        getFavorites()
    }


    fun getFavorites() {
        _screenState.value = FavoritesScreenState.Loading
        docRef.get().addOnSuccessListener { document ->
            (document.get("items") as? List<String>).takeIf { it.isNullOrEmpty().not() }
                ?.let { list ->
                    _screenState.value = FavoritesScreenState.Content(list.map {
                        gson.fromJson(
                            it,
                            MovieModel::class.java
                        )
                    })
                } ?: run {
                _screenState.value = FavoritesScreenState.Error("Your favorite list is empty")
            }
        }.addOnFailureListener {
            _screenState.value = FavoritesScreenState.Error("Error while loading your favorites")
        }
    }


    fun deleteToFavorites(movie: MovieModel) {
        docRef.get().addOnSuccessListener { document ->
            val favoritesList: ArrayList<String> =
                document.get("items") as? ArrayList<String> ?: arrayListOf()

            val gson = Gson()
            val movieString = gson.toJson(movie)

            favoritesList.remove(movieString)
            docRef.update("items", favoritesList)
            getFavorites()
        }
    }


}


sealed class FavoritesScreenState() {
    data object Loading : FavoritesScreenState()
    class Error(val error: String) : FavoritesScreenState()
    class Content(val movies: List<MovieModel>) : FavoritesScreenState()
}