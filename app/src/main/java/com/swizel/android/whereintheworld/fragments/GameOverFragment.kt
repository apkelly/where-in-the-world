package com.swizel.android.whereintheworld.fragments
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.games.Games
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.GoogleMapOptions
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.*
//import com.swizel.android.whereintheworld.Config
//import com.swizel.android.whereintheworld.R
//import com.swizel.android.whereintheworld.utils.AnalyticsUtils
//import com.swizel.android.whereintheworld.utils.ImageUtils
//import com.swizel.android.whereintheworld.viewmodels.WhereInTheWorldViewModel
////import kotlinx.android.synthetic.main.fragment_game_over.*
//
//
//class GameOverFragment : Fragment() {
//
//    private val viewModel: WhereInTheWorldViewModel by viewModels({ requireActivity() })
//
//    companion object {
//        private val MAP_CENTER = LatLng(25.0, 0.0)
//        private const val RC_ACHIEVEMENT_UI = 1234
//        private const val RC_LEADERBOARD_UI = 2345
//    }
//
//    private lateinit var mapFragment: SupportMapFragment
//    private var googleSignInAccount: GoogleSignInAccount? = null
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_game_over, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Add Map dynamically, xml instantiation of fragments inside fragments isn't supported (and causes crashes).
//        val options = GoogleMapOptions()
//        options.camera(CameraPosition.builder().target(MAP_CENTER).build())
//        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
//        options.compassEnabled(false)
//        options.rotateGesturesEnabled(false)
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
//                drawPlayerGuesses(googleMap)
//            }
//        }
//
//        val playerScore = viewModel.calculateScore()
//
//        val googleClientSignIn = GoogleSignIn.getClient(
//            requireContext(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestScopes(Games.SCOPE_GAMES_LITE)
//                .build()
//        )
//
////        // Enable Google Play Games buttons if we're signed in.
////        googleClientSignIn.silentSignIn()
////            .addOnSuccessListener { account ->
////                googleSignInAccount = account
////
////                Games.getLeaderboardsClient(requireContext(), account)
////                    .submitScore(Config.getLeaderboardId(viewModel.getGameDifficulty()!!), playerScore)
////
////                btn_achievements.isEnabled = true
////                btn_all_leaderboards.isEnabled = true
////                // TODO: Show a sign-out button.
////            }
////            .addOnFailureListener {
////                btn_achievements.isEnabled = false
////                btn_all_leaderboards.isEnabled = false
////                // TODO: Show a sign-in button
////            }
////
////        btn_achievements.setOnClickListener {
////            AnalyticsUtils.trackButtonClick(requireContext(), "Achievements")
////
////            // Launch generic Achievements screen
////            googleSignInAccount?.let { account ->
////                Games.getAchievementsClient(requireContext(), account)
////                    .achievementsIntent
////                    .addOnSuccessListener { intent ->
////                        startActivityForResult(intent, RC_ACHIEVEMENT_UI)
////                    }
////            }
////        }
////
////        btn_all_leaderboards.setOnClickListener {
////            AnalyticsUtils.trackButtonClick(requireContext(), "Leaderboards")
////
////            // Launch generic Leaderboard screen
////            googleSignInAccount?.let { account ->
////                Games.getLeaderboardsClient(requireContext(), account)
////                    .allLeaderboardsIntent
////                    .addOnSuccessListener { intent ->
////                        startActivityForResult(intent, RC_LEADERBOARD_UI)
////                    }
////            }
////        }
////
////        score.text = getString(R.string.score, playerScore)
//
//        AnalyticsUtils.trackGameEnd(requireContext(), viewModel.getGameDifficulty()!!)
//        AnalyticsUtils.trackScore(requireContext(), playerScore)
//
//        val ft = fragmentManager!!.beginTransaction()
//        ft.replace(R.id.mapStub, mapFragment)
//        ft.commit()
//    }
//
//    private fun drawPlayerGuesses(googleMap: GoogleMap) {
//        val markerIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_action_location)!!
//
//        // Draw locations for game.
//        viewModel.guesses.filterNotNull().forEachIndexed { index, guess ->
//            // Draw actual locations
//            googleMap.addMarker(
//                MarkerOptions().position(guess.panoramaLatLng)
//                    .anchor(Config.MAP_LOCATION_H_ANCHOR, Config.MAP_LOCATION_V_ANCHOR)
//                    .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.drawableToBitmap(markerIcon, index + 1)))
//            )
//
//            // Add hue to pin icon.
//            val pinIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_action_pin)!!.mutate()
//            // TODO: Give each player a colour, tint the drawable and match colour to scores.
//            // pinIcon.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
//
//            guess.guessedLatLng?.let { guessedLatLng ->
//                // Draw guesses for all players.
//                googleMap.addMarker(
//                    MarkerOptions().position(guessedLatLng)
//                        .anchor(Config.MAP_GUESS_H_ANCHOR, Config.MAP_GUESS_V_ANCHOR)
//                        .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.drawableToBitmap(pinIcon)))
//                )
//
//                // Draw lines.
//                val rectOptions = PolylineOptions()
//                    .add(guess.panoramaLatLng)
//                    .add(guessedLatLng)
//                    .width(resources.getDimensionPixelSize(R.dimen.game_over_line_width).toFloat())
//                googleMap.addPolyline(rectOptions)
//
//            }
//        }
//    }
//}