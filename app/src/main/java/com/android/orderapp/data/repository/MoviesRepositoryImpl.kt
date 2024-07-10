package com.android.orderapp.data.repository

import com.android.orderapp.data.model.MovieListResponse
import com.android.orderapp.data.model.MovieModel
import com.android.orderapp.data.service.TMDBApiService
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val tmdbApiService: TMDBApiService
) : BaseRepository(), MoviesRepository {
    override suspend fun getLatestMovies(page: Int): Result<MovieListResponse> = safeApiCall {
        tmdbApiService.getLatestMovie(page = page)
    }

    override suspend fun getMovieDetailsById(movieId: Int): Result<MovieModel> = safeApiCall {
        tmdbApiService.getMovieDetailsById(id = movieId)
    }

}