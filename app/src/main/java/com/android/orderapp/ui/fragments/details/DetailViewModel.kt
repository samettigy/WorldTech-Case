package com.android.orderapp.ui.fragments.details

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.orderapp.data.model.BasketInfo
import com.android.orderapp.data.model.MovieModel
import com.android.orderapp.data.repository.MoviesRepository
import com.android.orderapp.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val firebaseAuth: FirebaseAuth,
    private val gson: Gson,
    private val firebaseFirestore: FirebaseFirestore
) : BaseViewModel() {


    private val _movieDetails = MutableLiveData<MovieModel>()
    val movieDetails: LiveData<MovieModel> = _movieDetails

    private val _favoritesState = MutableLiveData<Boolean>()
    val favoritesState: LiveData<Boolean> = _favoritesState

    private val _basketState = MutableLiveData<Boolean>()
    val basketState: LiveData<Boolean> = _basketState

    private val _libraryState = MutableLiveData<Boolean>(false)
    val libraryState: LiveData<Boolean> = _libraryState


    val favoritelist: ArrayList<String> = arrayListOf()
    val basketlist: ArrayList<String> = arrayListOf()
    val libraryList: ArrayList<String> = arrayListOf()
    val currentUser = firebaseAuth.currentUser?.uid
    val libraryDocRef = firebaseFirestore.collection("libraries").document("$currentUser")
    val basketsDocRef = firebaseFirestore.collection("baskets").document("$currentUser")
    val favoritesDocRef = firebaseFirestore.collection("favorites").document("$currentUser")
    var libraryItemIndex = -1

    fun getMovieDetails(movieId: Int) = viewModelScope.launch {
        moviesRepository.getMovieDetailsById(movieId).onSuccess { movie ->
            _movieDetails.value = movie

            checkMovieFavoriteStatus()
            checkMovieBasketsStatus()
            checkMovieLibraryStatus()

        }.onFailure {
            Log.d("movies-test", "HomeViewModel.getMovies.Error ${it.message}")
        }
    }

    fun updateMovieBasketStatus() {
        var movieString = gson.toJson(_movieDetails.value)
        if (basketlist.contains(movieString)) {
            basketlist.remove(movieString)
        } else {
            basketlist.add(movieString)
        }
        basketsDocRef.update("items", basketlist).addOnSuccessListener {
            checkMovieBasketsStatus()
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)

        }
    }


    fun checkMovieBasketsStatus() {
        val movie = _movieDetails.value
        if (movie != null) {
            basketlist.clear()
            basketsDocRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    (document.get("items") as? List<String>).takeIf { it.isNullOrEmpty().not() }
                        ?.let { list ->
                            basketlist.addAll(list)
                            _basketState.value =
                                list.any {
                                    gson.fromJson(
                                        it,
                                        MovieModel::class.java
                                    ).id == movie.id
                                }
                        }
                } else {
                    basketsDocRef.set(mapOf<String, Any>()).addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }
                }

            }.addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
        }
    }

    fun checkMovieLibraryStatus() {
        val movie = _movieDetails.value
        if (movie != null) {
            libraryList.clear()
            libraryDocRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    (document.get("items") as? List<String>).takeIf { it.isNullOrEmpty().not() }
                        ?.let { list ->
                            libraryList.addAll(list)
                            list.forEachIndexed { index, s ->
                                if (gson.fromJson(s, MovieModel::class.java).id == movie.id) {
                                    _libraryState.value = true
                                    libraryItemIndex = index
                                }
                            }
                            _libraryState.value =
                                list.any {
                                    gson.fromJson(
                                        it,
                                        MovieModel::class.java
                                    ).id == movie.id
                                }
                        }
                } else {
                    libraryDocRef.set(mapOf<String, Any>()).addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }
                }

            }.addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
        }
    }


    fun checkMovieFavoriteStatus() {
        val movie = _movieDetails.value
        if (movie != null) {
            favoritesDocRef.get().addOnSuccessListener { document ->
                (document.get("items") as? List<String>).takeIf { it.isNullOrEmpty().not() }
                    ?.let { list ->
                        favoritelist.addAll(list)
                        _favoritesState.value = favoritelist.any {
                            gson.fromJson(
                                it,
                                MovieModel::class.java
                            ).id == movie.id
                        }
                    }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
        }
    }

    fun updateFavoriteStatus(isChecked: Boolean) {
        var movieString = gson.toJson(_movieDetails.value)
        if (isChecked) {
            favoritelist.add(movieString)
        } else {
            favoritelist.remove(movieString)
        }
        favoritesDocRef.update("items", favoritelist).addOnSuccessListener {
            checkMovieFavoriteStatus()
        }
    }

    fun updateMovieReview(review: String) = viewModelScope.launch {
        _movieDetails.value = _movieDetails.value?.copy(review = review)
        val newList = libraryList.set(libraryItemIndex, gson.toJson(_movieDetails.value))
        basketsDocRef.update("items", newList)
    }


}


/*    fun updateBasketStatus(movie: MovieModel) {
        val list: ArrayList<String> = arrayListOf()

        basketsDocRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                list.addAll((document.get("items") as? List<String>).orEmpty())
            } else {
                basketsDocRef.set(BasketInfo(items = listOf()))
            }

            var movieString = gson.toJson(movie)
            if (list.contains(movieString)) {
                Log.d("eklendi", "başarılı")
            } else {
                list.add(movieString)
            }

            basketsDocRef.update("items", list)

        }.addOnFailureListener {
            Log.e("lif", "$it")
        }

    }*/

