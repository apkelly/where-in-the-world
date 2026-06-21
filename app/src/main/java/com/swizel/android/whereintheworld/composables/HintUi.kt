package com.swizel.android.whereintheworld.composables

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.model.Hint
import java.util.Locale

internal data class SelectedHint(
    val hint: Hint,
    val value: String,
)

@Composable
internal fun HintSelectionDialog(
    onDismissRequest: () -> Unit,
    onHintSelected: (Hint) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        textContentColor = MaterialTheme.colorScheme.onSurface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        title = {
            Text(text = stringResource(id = R.string.hint_dialog_title))
        },
        text = {
            Column {
                Text(text = stringResource(id = R.string.hint_dialog_message))

                Hint.entries.filter { it != Hint.NONE }.forEach { hint ->
                    ListItem(
                        headlineContent = {
                            Text(text = hint.label())
                        },
                        supportingContent = {
                            Text(
                                text = stringResource(
                                    id = R.string.hint_score_multiplier,
                                    hint.multiplierText(),
                                ),
                            )
                        },
                        leadingContent = {
                            RadioButton(
                                selected = false,
                                onClick = null,
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = false,
                                onClick = { onHintSelected(hint) },
                                role = Role.RadioButton,
                            ),
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent,
                        ),
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
    )
}

internal fun Hint.toSelectedHint(
    country: String,
    landmark: String,
): SelectedHint? = when (this) {
    Hint.NONE -> null
    Hint.COUNTRY -> SelectedHint(hint = this, value = country)
    Hint.LANDMARK -> SelectedHint(hint = this, value = landmark)
}

@Composable
internal fun Hint.label(): String = when (this) {
    Hint.NONE -> stringResource(id = R.string.hint_none)
    Hint.COUNTRY -> stringResource(id = R.string.hint_country)
    Hint.LANDMARK -> stringResource(id = R.string.hint_landmark)
}

internal fun Hint.multiplierText(): String = String.format(Locale.US, "x%.2f", multiplier)
