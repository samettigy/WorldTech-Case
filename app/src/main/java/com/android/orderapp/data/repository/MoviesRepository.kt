package com.android.orderapp.data.repository

import com.android.orderapp.data.model.MovieListResponse
import com.android.orderapp.data.model.MovieModel

interface MoviesRepository {
    suspend fun getLatestMovies(page: Int): Result<MovieListResponse>

    suspend fun getMovieDetailsById(movieId : Int) : Result<MovieModel>
}