package com.android.orderapp.data.service

import com.android.orderapp.data.model.MovieListResponse
import com.android.orderapp.data.model.MovieModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiService {

    @GET("movie/popular")
    suspend fun getLatestMovie(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): Response<MovieListResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetailsById(
        @Path("movie_id") id: Int
    ): Response<MovieModel>

}