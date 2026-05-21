package com.swizel.android.whereintheworld.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey

interface ScreenSpec<in T : NavKey> {

    @Composable
    fun Content(
        arguments: T,
        navigateTo: (NavKey) -> Unit,
    )
}
