package com.swizel.android.whereintheworld.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameType

class GameDifficultyDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle(R.string.difficulty_dialog_title)
            .setItems(arrayOf("Easy", "Medium", "Hard", "Extreme")) { dialog, which ->
                val gameType = arguments!!.getSerializable("gameType") as GameType

                // FIXME: don't pass args in bundle, set them in a viewmodel.
                findNavController().navigate(R.id.nav_to_streetview, Bundle().apply {
                    putSerializable("gameType", gameType)
                    putSerializable("difficulty", getDifficultyForChoice(which))
                })

            }

        // Create dialog.
        val dialog = builder.create()

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

    private fun getDifficultyForChoice(choice: Int): GameDifficulty {
        return when (choice) {
            3 -> GameDifficulty.EXTREME
            2 -> GameDifficulty.HARD
            1 -> GameDifficulty.MEDIUM
            else -> GameDifficulty.EASY
        }
    }

}