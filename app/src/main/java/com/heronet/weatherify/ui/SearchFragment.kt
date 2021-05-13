package com.heronet.weatherify.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.heronet.weatherify.R
import com.heronet.weatherify.api.Weather
import com.heronet.weatherify.databinding.FragmentSearchBinding
import com.heronet.weatherify.extension.hideKeyboard
import com.heronet.weatherify.ui.viewmodels.SearchViewModel
import com.heronet.weatherify.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private lateinit var tempUnit: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            searchButton.setOnClickListener {
                it.hideKeyboard() // Extension
                val location = location.text.toString().trim()
                if(location.isNotEmpty())
                    viewModel.getWeather(location, tempUnit)
            }
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.units_array,
                android.R.layout.simple_spinner_item
            ).also {adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                unitSelector.adapter = adapter
                unitSelector.onItemSelectedListener = this@SearchFragment
            }
        }
        viewModel.weather.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.detailsTable.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                }
                is Resource.Success -> {
                    bind(binding, response.data!!)
                    binding.detailsTable.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding.tvError.text = response.message
                    binding.tvError.visibility = View.VISIBLE
                    binding.detailsTable.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
            }

        })
    }
    @SuppressLint("SetTextI18n")
    private fun bind(binding: FragmentSearchBinding, weather: Weather) {
        binding.apply {
            tvCity.text = weather.name
            tvHumidity.text = "${weather.main.humidity}%"
            tvType.text = weather.weather[0].main
            tvCloudiness.text = "${weather.clouds.all}%"
            when(tempUnit) {
                "metric" -> {
                    tvTemp.text = "${weather.main.temp}°C"
                    tvFeelsLike.text = "${weather.main.feelsLike}°C"
                    tvWindSpeed.text = "${weather.wind.speed} m/s"
                }
                "imperial" -> {
                    tvTemp.text = "${weather.main.temp}°F"
                    tvFeelsLike.text = "${weather.main.feelsLike}°F"
                    tvWindSpeed.text = "${weather.wind.speed} mph"
                }
                "standard" -> {
                    tvTemp.text = "${weather.main.temp}K"
                    tvFeelsLike.text = "${weather.main.feelsLike}K"
                    tvWindSpeed.text = "${weather.wind.speed} m/s"
                }
            }
        }

    }
    private fun unitSelector(unit: String) {
        when(unit) {
            "Celsius" -> tempUnit = "metric"
            "Fahrenheit" -> tempUnit = "imperial"
            "Kelvin" -> tempUnit = "standard"
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position)
        unitSelector(item.toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}