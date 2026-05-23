package com.swizel.android.whereintheworld.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

@Immutable
data class AppColorScheme(
    val mapOverlayContainer: Color,
    val mapOverlayStrongContainer: Color,
    val onMapOverlay: Color,
    val timerWarning: Color,
    val timerUrgent: Color,
    val actualLocationPin: Color,
    val guessLocationPin: Color,
)

@Composable
fun WhereInTheWorldTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    val appColors = if (darkTheme) {
        DarkAppColorScheme
    } else {
        LightAppColorScheme
    }

    CompositionLocalProvider(
        LocalAppColorScheme provides appColors,
        LocalWindowSizeClass provides currentWindowAdaptiveInfo().windowSizeClass,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}

object WhereInTheWorldTheme {
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val appColors: AppColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current
}

val LocalWindowSizeClass = compositionLocalOf<WindowSizeClass> {
    error("No LocalWindowSizeClass specified.")
}

private val LocalAppColorScheme = compositionLocalOf<AppColorScheme> {
    error("No LocalAppColorScheme specified.")
}

fun WindowSizeClass.isExpandedWidth(): Boolean = windowWidthSizeClass == WindowWidthSizeClass.EXPANDED

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xff3A9DD6),
    secondary = Color(0xffD6733A),
    tertiary = Color(0xff3ad6c1),
    background = Color(0xff222222),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xff3A9DD6),
    secondary = Color(0xffD6733A),
    tertiary = Color(0xff3ad6c1),
    background = Color.White,
)

private val DarkAppColorScheme = AppColorScheme(
    mapOverlayContainer = Color.Black.copy(alpha = 0.72f),
    mapOverlayStrongContainer = Color.Black.copy(alpha = 0.85f),
    onMapOverlay = Color.White,
    timerWarning = Color(0xFFFFD54F),
    timerUrgent = Color(0xFFFF5252),
    actualLocationPin = Color(0xFFFFD54F),
    guessLocationPin = Color(0xFFE040FB),
)

private val LightAppColorScheme = AppColorScheme(
    mapOverlayContainer = Color.Black.copy(alpha = 0.72f),
    mapOverlayStrongContainer = Color.Black.copy(alpha = 0.85f),
    onMapOverlay = Color.White,
    timerWarning = Color(0xFFFFC107),
    timerUrgent = Color(0xFFD32F2F),
    actualLocationPin = Color(0xFFFFC107),
    guessLocationPin = Color(0xFF9C27B0),
)
