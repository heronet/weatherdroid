package com.heronet.weatherify.util

import kotlinx.coroutines.flow.flow
import java.lang.Exception

fun<T> networkBoundResource(
    fetch: suspend () -> T
) = flow {
    emit(Resource.Loading<T>())
    try {
        emit(Resource.Success(fetch()))
    } catch (e: Exception) {
        emit(Resource.Error<T>("No Internet"))
    }
}