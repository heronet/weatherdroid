package com.heronet.weatherify.util

import android.util.Log
import kotlinx.coroutines.flow.flow
import java.lang.Exception

fun<T> networkBoundResource(
    fetch: suspend () -> T
) = flow {
    emit(Resource.Loading<T>())
    try {
        emit(Resource.Success(fetch()))
    } catch (e: Exception) {
        Log.d("ERRRRR", e.toString())
        emit(Resource.Error<T>("No Internet"))
    }
}