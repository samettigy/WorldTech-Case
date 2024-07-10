package com.android.orderapp.data.model

import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var results: ArrayList<MovieModel> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null
)