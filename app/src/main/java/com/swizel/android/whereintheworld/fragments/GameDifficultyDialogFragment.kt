package com.swizel.android.whereintheworld.fragments
//
//import android.app.AlertDialog
//import android.app.Dialog
//import android.os.Bundle
//import androidx.fragment.app.DialogFragment
//import androidx.fragment.app.viewModels
//import androidx.navigation.fragment.findNavController
//import com.swizel.android.whereintheworld.R
//import com.swizel.android.whereintheworld.model.GameDifficulty
//import com.swizel.android.whereintheworld.utils.AnalyticsUtils
//import com.swizel.android.whereintheworld.viewmodels.WhereInTheWorldViewModel
//
//class GameDifficultyDialogFragment : DialogFragment() {
//
//    private val viewModel: WhereInTheWorldViewModel by viewModels({ requireActivity() })
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder = AlertDialog.Builder(activity)
//
//        builder.setTitle(R.string.difficulty_dialog_title)
//            .setItems(
//                arrayOf(
//                    getString(R.string.difficulty_easy),
//                    getString(R.string.difficulty_medium),
//                    getString(R.string.difficulty_hard),
//                    getString(R.string.difficulty_extreme)
//                )
//            ) { _, which ->
//                val difficulty = getDifficultyForChoice(which)
//                viewModel.startGame(difficulty)
//                AnalyticsUtils.trackGameStart(requireContext(), difficulty)
//
//                findNavController().navigate(R.id.nav_to_streetview)
//            }
//
//        // Create dialog.
//        val dialog = builder.create()
//
//        dialog.setCancelable(false)
//        dialog.setCanceledOnTouchOutside(false)
//
//        return dialog
//    }
//
//    private fun getDifficultyForChoice(choice: Int): GameDifficulty {
//        return when (choice) {
//            3 -> GameDifficulty.EXTREME
//            2 -> GameDifficulty.HARD
//            1 -> GameDifficulty.MEDIUM
//            else -> GameDifficulty.EASY
//        }
//    }
//
//}
