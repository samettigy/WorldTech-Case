package com.android.orderapp.data.repository

import android.util.Log
import retrofit2.Response
import java.lang.Exception

abstract class BaseRepository {

    protected suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>
    ): Result<T> {
        return runCatching {
            val response = call.invoke()
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                return@runCatching responseBody
            }

            throw Exception()
        }
    }
}

