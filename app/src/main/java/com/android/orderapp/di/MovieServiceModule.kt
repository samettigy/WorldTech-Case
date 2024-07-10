package com.android.orderapp.di

import android.content.Context
import com.android.orderapp.data.service.TMDBApiService
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieServiceModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson().newBuilder().create()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideClient(
        @ApplicationContext context: Context
    ): OkHttpClient = OkHttpClient.Builder().apply {
        networkInterceptors().add(Interceptor {
            val requestBuilder = it.request().newBuilder()
            requestBuilder.header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlMmY4NDllOGUzM2JmYzgyNGNhN2Q2NzM0ZjI5YjRiZSIsInN1YiI6IjY1YzRhZGY0ZmQ2ZmExMDE2ZTc5MTVmOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.fDTPz9Sq4RAKGJOuOJ0PcJnZQkfXvO5tizPHfmN4mSQ"
            );
            it.proceed(requestBuilder.build());
        })
        addInterceptor(ChuckerInterceptor(context))
    }.build()

    @Singleton
    @Provides
    fun provideTmdbService(
        gson: Gson,
        okHttpClient: OkHttpClient,
    ): TMDBApiService {
        return Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://api.themoviedb.org/3/").build()
            .create(TMDBApiService::class.java)
    }
}

const val imageBase: String = "https://image.tmdb.org/t/p/w500/"
