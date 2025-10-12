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
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

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

    val context = LocalContext.current
//    val previewHandler = AsyncImagePreviewHandler {
//        ContextCompat.getDrawable(context, R.drawable.preview_image)!!.asImage()
//    }

    CompositionLocalProvider(
//        LocalAsyncImagePreviewHandler provides previewHandler,
        LocalWindowSizeClass provides currentWindowAdaptiveInfo().windowSizeClass,
//        LocalSizes provides StubzSizes(),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
//            typography = stubzTypography,
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
}

val LocalWindowSizeClass = compositionLocalOf<WindowSizeClass> {
    error("No LocalWindowSizeClass specified.")
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