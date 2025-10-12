package com.swizel.android.whereintheworld.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey

abstract class ScreenSpec<in T : NavKey> {

    @Composable
    abstract fun Content(
        arguments: T,
        navigateTo: (NavKey) -> Unit,
    )
}
