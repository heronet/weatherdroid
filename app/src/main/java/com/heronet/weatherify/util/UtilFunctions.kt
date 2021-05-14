package com.heronet.weatherify.util

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.ImageView
import com.heronet.weatherify.R
import pub.devrel.easypermissions.EasyPermissions

object UtilFunctions {
    fun hasLocationPermissions(context: Context) =
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    fun imageSelector(icon: String, imageView: ImageView) {
        when(icon) {
            "01d" -> imageView.setImageResource(R.drawable.ic_01d)
            "02d" -> imageView.setImageResource(R.drawable.ic_02d)
            "03d" -> imageView.setImageResource(R.drawable.ic_03d)
            "04d" -> imageView.setImageResource(R.drawable.ic_03d)
            "09d" -> imageView.setImageResource(R.drawable.ic_09d)
            "10d" -> imageView.setImageResource(R.drawable.ic_10d)
            "11d" -> imageView.setImageResource(R.drawable.ic_11d)
            "13d" -> imageView.setImageResource(R.drawable.ic_13d)
            "50d" -> imageView.setImageResource(R.drawable.ic_50d)

            "01n" -> imageView.setImageResource(R.drawable.ic_01d)
            "02n" -> imageView.setImageResource(R.drawable.ic_02d)
            "03n" -> imageView.setImageResource(R.drawable.ic_03d)
            "04n" -> imageView.setImageResource(R.drawable.ic_03d)
            "09n" -> imageView.setImageResource(R.drawable.ic_09d)
            "10n" -> imageView.setImageResource(R.drawable.ic_10d)
            "11n" -> imageView.setImageResource(R.drawable.ic_11d)
            "13n" -> imageView.setImageResource(R.drawable.ic_13d)
            "50n" -> imageView.setImageResource(R.drawable.ic_50d)
        }
    }
}