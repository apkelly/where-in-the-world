package com.swizel.android.whereintheworld.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.StreetViewPanoramaOptions
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng
import com.swizel.android.whereintheworld.Config
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.utils.AnalyticsUtils
import com.swizel.android.whereintheworld.viewmodels.WhereInTheWorldViewModel
import kotlinx.android.synthetic.main.fragment_streetview.*
import java.util.concurrent.TimeUnit


class StreetViewFragment : Fragment() {

    private val viewModel: WhereInTheWorldViewModel by viewModels({ requireActivity() })

    private lateinit var streetViewFragment: SupportStreetViewPanoramaFragment
    private lateinit var streetViewPanorama: StreetViewPanorama
    private lateinit var countDownTimer: CountDownTimer

    private val originalTimeMillis: Long = 0
    private var locationUpdated = false
    private var countdownTimerStarted = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_streetview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options = StreetViewPanoramaOptions().apply {
            streetNamesEnabled(false)
            position(LatLng(-33.8688, 151.2093), 500) // FIXME: Sydney
        }

        streetViewFragment = SupportStreetViewPanoramaFragment.newInstance(options).apply {
            getStreetViewPanoramaAsync { panorama ->
                System.out.println("getStreetViewPanoramaAsync")

                streetViewPanorama = panorama.apply {
                    setOnStreetViewPanoramaChangeListener { streetViewPanoramaLocation ->
                        if (streetViewPanoramaLocation != null) {
                            System.out.println("streetViewPanoramaLocation : $streetViewPanoramaLocation")
                            streetViewLocationAcquired(streetViewPanoramaLocation.position)
                        }
                    }
                }
            }
        }

        round_number.text = "${viewModel.currentRound + 1}/${Config.MAX_ROUNDS}"
        btn_guess.setOnClickListener {
            AnalyticsUtils.trackButtonClick(requireContext(), "onGuessClicked")

            findNavController().navigate(R.id.nav_to_guess_location)
        }

        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.streetViewStub, streetViewFragment)
        ft.commit()
    }

    override fun onPause() {
        super.onPause()

        stopTimer()

//        mHandler.removeCallbacks(mLocationUpdate)
    }

    override fun onResume() {
        super.onResume()

        startTimer()

        // TODO: Resume mHandler callbacks??
    }

    private fun stopTimer() {
        // Cancel timer
        countDownTimer.cancel()
        countdownTimerStarted = false
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(viewModel.remainingStreetViewTimer, 10) {
            override fun onTick(millis: Long) {
                var millisUntilFinished = millis
                countdownTimerStarted = true

                viewModel.remainingStreetViewTimer = millisUntilFinished

                setTimerColor(millisUntilFinished)

                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                millisUntilFinished -= TimeUnit.SECONDS.toMillis(seconds)

                countdown_timer.text = String.format("%02d:%02d.%03d", minutes, seconds, millisUntilFinished)
            }

            override fun onFinish() {
                toggleStreetViewEnabled(false)
                setTimerColor(0)
                countdown_timer.text = "00:00.000"
            }
        }

        if (locationUpdated && !countdownTimerStarted) {
            toggleStreetViewEnabled(true)
            btn_guess.isEnabled = true
            countDownTimer.start()
        }
    }

    private fun setTimerColor(millisUntilFinished: Long) {
        val halfTime = originalTimeMillis / 2
        val quarterTime = originalTimeMillis / 4

        when {
            millisUntilFinished < quarterTime -> countdown_timer.setTextColor(Color.RED)
            millisUntilFinished < halfTime -> countdown_timer.setTextColor(Color.YELLOW)
            else -> countdown_timer.setTextColor(Color.WHITE)
        }
    }

    private fun streetViewLocationAcquired(location: LatLng) {
//        if (!locationUpdated) {
        System.out.println("streetViewLocationAcquired : $location")
//            GameState.getInstance().updateLocationForCurrentRound(location)
//            locationUpdated = true
//            mHandler.removeCallbacks(mLocationUpdate)

        if (!countdownTimerStarted) {
            toggleStreetViewEnabled(true)
            btn_guess.isEnabled = true

            countDownTimer.start()
        }
//        }
    }

    private fun toggleStreetViewEnabled(enabled: Boolean) {
        streetViewPanorama.isPanningGesturesEnabled = enabled
        streetViewPanorama.isUserNavigationEnabled = enabled
        streetViewPanorama.isZoomGesturesEnabled = enabled
//        shroud.setVisibility(if (!enabled) View.VISIBLE else View.GONE)
    }
}