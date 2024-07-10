package com.android.orderapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.orderapp.data.adapter.MovieAdapter
import com.android.orderapp.data.model.MovieListResponse
import com.android.orderapp.data.model.MovieModel
import com.android.orderapp.data.repository.MoviesRepository
import com.android.orderapp.data.repository.MoviesRepositoryImpl

class MoviesPagingSource(private val repository: MoviesRepository) :
    PagingSource<Int, MovieModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        TODO()
    }

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        TODO("Not yet implemented")
    }


}