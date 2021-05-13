package com.heronet.weatherify.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.heronet.weatherify.adapter.WeatherForecastAdapter
import com.heronet.weatherify.databinding.FragmentSummaryBinding
import com.heronet.weatherify.ui.viewmodels.SummaryViewModel
import com.heronet.weatherify.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummaryFragment : Fragment() {
    private val viewModel: SummaryViewModel by viewModels()
    private lateinit var binding: FragmentSummaryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = WeatherForecastAdapter()
        binding.rvForecast.adapter = adapter
        viewModel.forecast.observe(viewLifecycleOwner, {resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.apply {
                        summaryProgress.visibility = View.VISIBLE
                        rvForecast.visibility = View.GONE
                        clErrorLayout.visibility = View.GONE
                    }
                }
                is Resource.Success -> {
                    binding.apply {
                        adapter.submitList(resource.data?.daily)
                        rvForecast.visibility = View.VISIBLE
                        summaryProgress.visibility = View.GONE
                        clErrorLayout.visibility = View.GONE
                    }
                }
                is Resource.Error -> {
                    binding.apply {
                        summaryProgress.visibility = View.GONE
                        rvForecast.visibility = View.GONE
                        tvError.text = resource.message
                        clErrorLayout.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

}