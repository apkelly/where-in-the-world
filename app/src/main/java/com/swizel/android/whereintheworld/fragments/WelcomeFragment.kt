package com.swizel.android.whereintheworld.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.model.GameType
import com.swizel.android.whereintheworld.utils.AnalyticsUtils
import com.swizel.android.whereintheworld.utils.MultiTransitionDrawable
import com.swizel.android.whereintheworld.viewmodels.WhereInTheWorldViewModel
import kotlinx.android.synthetic.main.fragment_welcome.*


class WelcomeFragment : Fragment() {
    private val viewModel: WhereInTheWorldViewModel by viewModels({ requireActivity() })

    private lateinit var multiTransitionDrawable: MultiTransitionDrawable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backgroundIds: IntArray
//        if (resources.getBoolean(R.bool.landscape)) {
//            backgroundIds = intArrayOf(R.drawable.welcome_rushmore, R.drawable.welcome_taj_mahal, R.drawable.welcome_pyramids)
//        } else {
        backgroundIds = intArrayOf(R.drawable.welcome_eiffel, R.drawable.welcome_pisa, R.drawable.welcome_christ)
//        }

        multiTransitionDrawable = MultiTransitionDrawable(welcome_background, backgroundIds)


        btn_solo_mode.setOnClickListener {
            AnalyticsUtils.trackButtonClick(requireContext(), "SoloGame")
            viewModel.gameType = GameType.SOLO

            findNavController().navigate(R.id.nav_to_dialog)
        }

        btn_quick_challenge_mode.setOnClickListener {
            AnalyticsUtils.trackButtonClick(requireContext(), "QuickChallenge")
        }

        btn_friend_challenge_mode.setOnClickListener {
            AnalyticsUtils.trackButtonClick(requireContext(), "FriendChallenge")
        }

    }


    override fun onPause() {
        super.onPause()

        multiTransitionDrawable.onPause()
    }

    override fun onResume() {
        super.onResume()

        multiTransitionDrawable.onResume()
    }

}