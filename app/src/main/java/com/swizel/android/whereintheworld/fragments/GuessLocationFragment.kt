package com.swizel.android.whereintheworld.fragments
//
//import android.animation.Animator
//import android.animation.ValueAnimator
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.Color
//import android.os.Bundle
//import android.os.Handler
//import android.util.TypedValue
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.animation.BounceInterpolator
//import android.widget.TextView
//import androidx.annotation.DrawableRes
//import androidx.appcompat.content.res.AppCompatResources
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.navigation.fragment.findNavController
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.GoogleMapOptions
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.*
//import com.swizel.android.whereintheworld.Config
//import com.swizel.android.whereintheworld.R
//import com.swizel.android.whereintheworld.utils.SettingsUtils
//import com.swizel.android.whereintheworld.viewmodels.WhereInTheWorldViewModel
//import androidx.core.graphics.createBitmap
//
//
//class GuessLocationFragment : Fragment() {
//
//    private val viewModel: WhereInTheWorldViewModel by viewModels({ requireActivity() })
//
//    companion object {
//        private val MAP_CENTER = LatLng(25.0, 0.0)
//        private val TUTORIAL_PIN = LatLng(15.0, 0.0)
//    }
//
//    private lateinit var mapFragment: SupportMapFragment
//    private lateinit var infoWindowAdapter: MyInfoWindowAdapter
//    private var droppedPin: Marker? = null
//    private var tutorialPin = false
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_guess_location, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        infoWindowAdapter = MyInfoWindowAdapter()
//
//        val options = GoogleMapOptions()
//        options.camera(CameraPosition.builder().target(MAP_CENTER).build())
//        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
//        options.compassEnabled(false)
//        options.rotateGesturesEnabled(true)
//        options.zoomControlsEnabled(true)
//        options.zoomGesturesEnabled(true)
//        options.mapToolbarEnabled(false)
//        options.tiltGesturesEnabled(false)
//
//        mapFragment = SupportMapFragment.newInstance(options).apply {
//            getMapAsync { googleMap ->
//                googleMap.uiSettings.isMyLocationButtonEnabled = false
//                googleMap.isIndoorEnabled = false
//
//                googleMap.setOnInfoWindowClickListener { marker ->
//                    if (!tutorialPin) {
//                        marker.hideInfoWindow()
//                        confirmGuessClicked(marker.position)
//                        //  getListener().onConfirmGuessClicked(marker.position, arguments!!.getLong("timeTaken"))
//                    }
//                }
//                googleMap.setInfoWindowAdapter(infoWindowAdapter)
//                googleMap.setOnMapLongClickListener { latLng ->
//                    droppedPin?.apply {
//                        if (isInfoWindowShown) {
//                            hideInfoWindow()
//                        }
//                        remove()
//                    }
//
//                    infoWindowAdapter.setSnippetResId(R.string.confirm_guess)
//                    droppedPin = googleMap.addMarker(createMarkerOptions(latLng))
//                    tutorialPin = false
//
//                    // Remove tutorial after user has managed to drop their first pin.
//                    SettingsUtils.addPreference(requireContext(), SettingsUtils.FIRST_EVER_GUESS, false)
//
//                    // Animate pin into position.
//                    dropPinEffect(droppedPin)
//                }
//
//                // Show a tutorial pin for the first ever guess.
//                val firstEverGuess = SettingsUtils.getBooleanPreference(requireContext(), SettingsUtils.FIRST_EVER_GUESS, true)
//                if (firstEverGuess) {
//                    infoWindowAdapter.setSnippetResId(R.string.tutorial_snippet)
//                    droppedPin = googleMap.addMarker(createMarkerOptions(TUTORIAL_PIN))
//                    droppedPin?.showInfoWindow()
//                    tutorialPin = true
//                }
//
//            }
//        }
//
//        val ft = requireFragmentManager().beginTransaction()
//        ft.replace(R.id.mapStub, mapFragment)
//        ft.commit()
//    }
//
//    private fun confirmGuessClicked(location: LatLng) {
//        // Set guessed location for current round.
////        GameState.getInstance().updateGuessForCurrentRound(location, timeTaken)
//        viewModel.setGuessForCurrentRound(location, 0L)
//
//        val nextRound = viewModel.configureNextRound()
//        if (nextRound < viewModel.getNumRounds()) {
//            findNavController().navigate(R.id.nav_to_streetview)
//        } else {
//
//            findNavController().navigate(R.id.nav_to_game_over)
//        }
//    }
//
//    private fun createMarkerOptions(location: LatLng): MarkerOptions {
//        return MarkerOptions().position(location)
//            .anchor(Config.MAP_GUESS_H_ANCHOR, Config.MAP_GUESS_V_ANCHOR)
//            .infoWindowAnchor(Config.MAP_INFO_H_ANCHOR, Config.MAP_INFO_V_ANCHOR)
//            .also {
//                getBitmapFromVectorDrawable(R.drawable.ic_action_pin)?.let { bitmap ->
//                    it.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
//                }
//            }
//    }
//
//    private fun getBitmapFromVectorDrawable(@DrawableRes drawableId: Int): Bitmap? {
//        val drawable = AppCompatResources.getDrawable(requireContext(), drawableId)
//        return if (drawable != null) {
//            val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
//            val canvas = Canvas(bitmap);
//            drawable.setBounds(0, 0, canvas.width, canvas.height)
//            drawable.draw(canvas)
//
//            bitmap
//        } else {
//            null
//        }
//    }
//
//    private fun dropPinEffect(marker: Marker?) {
//        val valueAnimator = ValueAnimator.ofFloat(Config.MAP_GUESS_V_ANCHOR * 5, Config.MAP_GUESS_V_ANCHOR)
//        valueAnimator.duration = Config.MAPPING_DROPPED_PIN_ANIMATION_SPEED_MS
//        valueAnimator.interpolator = BounceInterpolator()
//        valueAnimator.addListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator) {
//
//            }
//
//            override fun onAnimationEnd(animation: Animator) {
//                Handler().postDelayed({ marker?.showInfoWindow() }, 250)
//            }
//
//            override fun onAnimationCancel(animation: Animator) {
//
//            }
//
//            override fun onAnimationRepeat(animation: Animator) {
//
//            }
//        })
//        valueAnimator.addUpdateListener { animation ->
//            val offset = (animation.animatedValue as Float).toFloat()
//            marker?.setAnchor(Config.MAP_GUESS_H_ANCHOR, offset)
//        }
//        valueAnimator.start()
//    }
//
//    private inner class MyInfoWindowAdapter : GoogleMap.InfoWindowAdapter {
//        private var snippetResId: Int = 0
//
//        fun setSnippetResId(resId: Int) {
//            snippetResId = resId
//        }
//
//        override fun getInfoWindow(marker: Marker): View? {
//            return null
//        }
//
//        override fun getInfoContents(marker: Marker): View {
//            val padding = resources.getDimensionPixelSize(R.dimen.window_adapter_padding)
//
//            val textView = TextView(activity)
//            textView.setTextColor(Color.DKGRAY)
//            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.window_adatper_text_size))
//            textView.text = getString(snippetResId)
//            textView.setPadding(padding, padding, padding, padding)
//            return textView
//        }
//    }
//}