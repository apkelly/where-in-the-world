package com.swizel.android.whereintheworld.fragments

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.StreetViewPanoramaOptions
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng
import com.swizel.android.whereintheworld.R



class StreetViewFragment : Fragment() {

    private lateinit var streetViewFragment: SupportStreetViewPanoramaFragment
    private lateinit var streetViewPanorama: StreetViewPanorama

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_streetview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options = StreetViewPanoramaOptions().apply {
            streetNamesEnabled(false)
            position(LatLng(-33.8688,151.2093), 500) // FIXME: Sydney
        }

        streetViewFragment = SupportStreetViewPanoramaFragment.newInstance(options).apply {
            getStreetViewPanoramaAsync { panorama ->
                System.out.println("getStreetViewPanoramaAsync")
                //            roundNumber.setText(GameState.getInstance().getCurrentRound() + 1 + "/" + Config.MAX_ROUNDS)

                streetViewPanorama = panorama.apply {
                    setOnStreetViewPanoramaChangeListener { streetViewPanoramaLocation ->
                        if (streetViewPanoramaLocation != null) {
                            System.out.println("streetViewPanoramaLocation : $streetViewPanoramaLocation")
                        }
                    }
                }
            }
        }


        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.streetViewStub, streetViewFragment)
        ft.commit()
    }

    private fun toggleStreetViewEnabled(enabled: Boolean) {
        streetViewPanorama.isPanningGesturesEnabled = enabled
        streetViewPanorama.isUserNavigationEnabled = enabled
        streetViewPanorama.isZoomGesturesEnabled = enabled
//        shroud.setVisibility(if (!enabled) View.VISIBLE else View.GONE)
    }
}