package com.swizel.android.whereintheworld.composables

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.LocalContentColor

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
    val contentColor = LocalContentColor.current
    val isDarkTheme = isSystemInDarkTheme()
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors().copy(
            disabledContentColor = contentColor.copy(alpha = if (isDarkTheme) 0.62f else 0.74f),
            disabledContainerColor = contentColor.copy(alpha = if (isDarkTheme) 0.14f else 0.18f),
        ),
        content = content,
    )
}
