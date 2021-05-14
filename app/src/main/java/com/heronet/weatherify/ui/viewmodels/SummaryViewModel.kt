package com.heronet.weatherify.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.heronet.weatherify.api.Forecast
import com.heronet.weatherify.data.Repository
import com.heronet.weatherify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private fun unixToLocal(unix: Int): String {
        val time = unix * 1000.toLong()
        val date = Date(time)
        val format = SimpleDateFormat("EEEE MMM dd")
        format.timeZone = TimeZone.getTimeZone("GMT")
        return format.format(date)
    }
    fun requestForecast(lat: Number, lon: Number): LiveData<Resource<Forecast>> {
        val data = repository.getForecast(lat, lon).map { res ->
            res.data?.daily?.map { daily ->
                daily.dateFormatted = unixToLocal(daily.dt)
            }
            return@map res
        }.asLiveData()
        return data
    }
}