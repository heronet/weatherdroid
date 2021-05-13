package com.heronet.weatherify.ui.viewmodels

import androidx.lifecycle.*
import com.heronet.weatherify.api.Forecast
import com.heronet.weatherify.data.Repository
import com.heronet.weatherify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    private val _forecast = repository.getForecast(24.7936, 88.9318).map {
        it.data?.daily?.map { daily ->
            daily.dateFormatted = unixToLocal(daily.dt)
        }
        return@map it
    }.asLiveData()
    val forecast: LiveData<Resource<Forecast>> = _forecast

    private fun unixToLocal(unix: Int): String {
        val time = unix * 1000.toLong()
        val date = Date(time)
        val format = SimpleDateFormat("EEEE MMM dd")
        format.timeZone = TimeZone.getTimeZone("GMT")
        return format.format(date)
    }
}