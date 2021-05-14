package com.heronet.weatherify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.heronet.weatherify.R
import com.heronet.weatherify.api.Forecast
import com.heronet.weatherify.databinding.WeatherItemBinding
import com.heronet.weatherify.util.UtilFunctions

class WeatherForecastAdapter: ListAdapter<Forecast.Daily, WeatherForecastAdapter.DailyViewHolder>(DiffUtilCb()) {
     class DailyViewHolder(private val binding: WeatherItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(forecastDaily: Forecast.Daily) {
            binding.apply {
                val dayAndDate = forecastDaily.dateFormatted?.split(" ".toRegex(), 2) // Split Day and Date
                tvDay.text = dayAndDate?.get(0)
                tvDate.text = dayAndDate?.get(1)
                tvTemp.text = "${forecastDaily.temp.day} °C"
                tvWeatherType.text = forecastDaily.weather[0].main
                tvWeatherDescription.text = forecastDaily.weather[0].description

                UtilFunctions.imageSelector(forecastDaily.weather[0].icon, ivWeather)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val binding = WeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
class DiffUtilCb: DiffUtil.ItemCallback<Forecast.Daily>() {
    override fun areItemsTheSame(oldItem: Forecast.Daily, newItem: Forecast.Daily): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(oldItem: Forecast.Daily, newItem: Forecast.Daily): Boolean {
        return oldItem.temp.eve == newItem.temp.eve
    }


}