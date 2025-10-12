//package com.swizel.android.whereintheworld.fragments
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.navigation.fragment.findNavController
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.games.Games
//import com.swizel.android.whereintheworld.R
//import com.swizel.android.whereintheworld.model.GameType
//import com.swizel.android.whereintheworld.utils.AnalyticsUtils
//import com.swizel.android.whereintheworld.utils.MultiTransitionDrawable
//import com.swizel.android.whereintheworld.viewmodels.WhereInTheWorldViewModel
////import kotlinx.android.synthetic.main.fragment_welcome.*
//
//
//class WelcomeFragment : Fragment() {
//
//    companion object {
//        private const val RC_ACHIEVEMENT_UI = 1234
//        private const val RC_LEADERBOARD_UI = 2345
//    }
//
//    private val viewModel: WhereInTheWorldViewModel by viewModels({ requireActivity() })
//
//    private lateinit var multiTransitionDrawable: MultiTransitionDrawable
//
//    private var googleSignInAccount: GoogleSignInAccount? = null
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_welcome, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val backgroundIds = if (resources.getBoolean(R.bool.landscape)) {
//            intArrayOf(R.drawable.welcome_rushmore, R.drawable.welcome_taj_mahal, R.drawable.welcome_pyramids)
//        } else {
//            intArrayOf(R.drawable.welcome_eiffel, R.drawable.welcome_pisa, R.drawable.welcome_christ)
//        }
//
//        multiTransitionDrawable = MultiTransitionDrawable(view.findViewById(R.id.welcome_background), backgroundIds)
////
////        btn_solo_mode.setOnClickListener {
////            AnalyticsUtils.trackButtonClick(requireContext(), "SoloGame")
////            viewModel.gameType = GameType.SOLO
////
////            findNavController().navigate(R.id.nav_to_dialog)
////        }
////
////        btn_quick_challenge_mode.setOnClickListener {
////            AnalyticsUtils.trackButtonClick(requireContext(), "QuickChallenge")
////        }
////
////        btn_friend_challenge_mode.setOnClickListener {
////            AnalyticsUtils.trackButtonClick(requireContext(), "FriendChallenge")
////        }
////
////        val googleClientSignIn = GoogleSignIn.getClient(
////            requireContext(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
////                .requestScopes(Games.SCOPE_GAMES_LITE)
////                .build()
////        )
////
////        // Enable Google Play Games buttons if we're signed in.
////        googleClientSignIn.silentSignIn()
////            .addOnSuccessListener { account ->
////                googleSignInAccount = account
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
//    }
//
//    override fun onPause() {
//        super.onPause()
//
//        multiTransitionDrawable.onPause()
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        multiTransitionDrawable.onResume()
//    }
//
//}