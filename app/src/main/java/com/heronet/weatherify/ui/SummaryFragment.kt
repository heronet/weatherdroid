package com.heronet.weatherify.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.heronet.weatherify.adapter.WeatherForecastAdapter
import com.heronet.weatherify.databinding.FragmentSummaryBinding
import com.heronet.weatherify.ui.viewmodels.SummaryViewModel
import com.heronet.weatherify.util.Resource
import com.heronet.weatherify.util.UtilFunctions
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


@AndroidEntryPoint
class SummaryFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private val viewModel: SummaryViewModel by activityViewModels()
    private lateinit var binding: FragmentSummaryBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fusedLocationProviderClient = FusedLocationProviderClient(requireContext())
        binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()

    }

    private fun requestPermissions() {
        if (UtilFunctions.hasLocationPermissions(requireContext())) {
            getLocationAndObserve()
            Log.d("PERM", "PERM GRANTEd")
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "Permit Location for usability",
                0,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Permit Location for usability",
                0,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }
    @SuppressLint("MissingPermission")
    private fun getLocationAndObserve() {
        binding.summaryProgress.visibility = View.VISIBLE
        val location = fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener {location ->
            if (location != null) {
                binding.summaryProgress.visibility = View.GONE
                startObserving(location.latitude, location.longitude)
            }
            else {
                binding.tvError.text = "Couldn't fetch Location. Make sure your location is enabled"
                binding.summaryProgress.visibility = View.GONE
                binding.clErrorLayout.visibility = View.VISIBLE

                // Another way of doing this
//                binding.tvError.text = "Fallback Fetch Location"
//                binding.clErrorLayout.visibility = View.VISIBLE
//                val locationRequest: LocationRequest = LocationRequest.create();
//                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
//                locationRequest.interval = 200 * 1000;
//                val locationCallback = object : LocationCallback() {
//                    override fun onLocationResult(locationResult: LocationResult) {
//                        for (loc in locationResult.locations) {
//                            if (loc != null) {
//                                binding.summaryProgress.visibility = View.GONE
//                                binding.clErrorLayout.visibility = View.GONE
//                                startObserving(loc.latitude, loc.longitude)
//                            } else {
//                                binding.summaryProgress.visibility = View.GONE
//                                binding.tvError.text = "Could Not Fetch Location"
//                                binding.clErrorLayout.visibility = View.VISIBLE
//                            }
//                        }
//                    }
//                }
//                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }
        }.addOnFailureListener {

        }

    }
    private fun startObserving(lat:Number, lon: Number) {
        viewModel.requestForecast(lat, lon).observe(viewLifecycleOwner, Observer {resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.apply {
                        summaryProgress.visibility = View.VISIBLE
                        rvForecast.visibility = View.GONE
                        clErrorLayout.visibility = View.GONE
                        clCity.visibility = View.GONE
                    }
                }
                is Resource.Success -> {
                    val data = resource.data
                    val adapter = WeatherForecastAdapter()
                    binding.rvForecast.adapter = adapter
                    binding.apply {
                        adapter.submitList(data?.daily?.drop(1)) // Remove the first element from the list. We treat it differently
                        rvForecast.visibility = View.VISIBLE
                        summaryProgress.visibility = View.GONE
                        clErrorLayout.visibility = View.GONE

                        // City Stuff
                        tvCity.text = data?.cityName
                        tvCityTemp.text = "${data?.current?.temp} °C"
                        tvCityType.text = data?.current?.weather?.get(0)?.main
                        tvCityDesc.text = data?.current?.weather?.get(0)?.description
                        UtilFunctions.imageSelector(data?.current?.weather?.get(0)?.icon.toString(), ivCity)

                        clCity.visibility = View.VISIBLE
                    }
                }
                is Resource.Error -> {
                    binding.apply {
                        summaryProgress.visibility = View.GONE
                        rvForecast.visibility = View.GONE
                        tvError.text = resource.message
                        clErrorLayout.visibility = View.VISIBLE
                        clCity.visibility = View.GONE
                    }
                }
            }
        })
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d("NPERM", "NEW PERM GRANTEd")
        getLocationAndObserve()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}