package com.swizel.android.whereintheworld.composables

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * App-wide standard button that enforces consistent theme-aware disabled-state colours across all screens.
 */
@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors().copy(
            disabledContentColor = colorScheme.onSurface.copy(alpha = 0.38f),
            disabledContainerColor = colorScheme.onSurface.copy(alpha = 0.12f),
        ),
        content = content,
    )
}
