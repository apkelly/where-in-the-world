package com.swizel.android.whereintheworld.viewmodels

import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.swizel.android.whereintheworld.BuildConfig
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameType
import com.swizel.android.whereintheworld.model.Guess
import com.swizel.android.whereintheworld.model.Hint
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*
import java.util.concurrent.TimeUnit

class WhereInTheWorldViewModel : ViewModel() {

    private var remoteConfig = FirebaseRemoteConfig.getInstance().apply {
        if (BuildConfig.DEBUG) {
            setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(TimeUnit.MINUTES.toSeconds(10))
                    .build()
            )
        }
        setDefaultsAsync(R.xml.remote_config_defaults)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Defaults Succeeded : ${getString("easy_config")}")
                } else {
                    println("Defaults failed")
                }
            }
    }

    init {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Fetch Succeeded : ${remoteConfig.getString("easy_config")}")
                } else {
                    println("Fetch failed")
                }
            }
    }

//    fun getRadius(): Int {
//        return gameConfig?.getInt("radius_m") ?: throw IllegalStateException("No game config.")
//    }

//    fun getLocation(): LatLng {
//        val locations = gameConfig?.getJSONArray("locations")
//        locations?.let {
//            val index = Random().nextInt(it.length())
//            val location = it.getJSONObject(index)
//            return LatLng(location.getDouble("lat"), location.getDouble("lon"))
//        }
//
//        throw IllegalStateException("Didn't get a location")
//    }



}
