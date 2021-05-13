package com.heronet.weatherify.ui.viewmodels

import androidx.lifecycle.*
import com.heronet.weatherify.api.Weather
import com.heronet.weatherify.data.Repository
import com.heronet.weatherify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private var _weather = MutableLiveData<Resource<Weather>>()
    val weather: LiveData<Resource<Weather>> get() = _weather

    fun getWeather(location: String, unit: String) {
        viewModelScope.launch {
            _weather.value = Resource.Loading()
            try {
                val response = repository.getCurrentInfo(location, unit)
                handleWeatherResponse(response)
            } catch (e: Exception) { // Most likely internet error
                _weather.value = Resource.Error("No Internet")
            }

        }
    }
    private fun handleWeatherResponse(response: Response<Weather>) {
        if (response.isSuccessful) {
            _weather.value = Resource.Success(response.body()!!)
        } else {
            _weather.value = Resource.Error(response.message())
        }
    }
}