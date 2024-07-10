package com.android.orderapp.di

import com.android.orderapp.data.repository.MoviesRepositoryImpl
import com.android.orderapp.data.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryProvider {

    @Binds
    abstract fun bindMoviesRepository(repository: MoviesRepositoryImpl): MoviesRepository
}