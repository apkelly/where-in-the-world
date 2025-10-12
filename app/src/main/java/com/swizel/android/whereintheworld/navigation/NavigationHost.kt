package com.swizel.android.whereintheworld.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

@Composable
fun NavigationHost(
    onDestinationChanged: (NavKey) -> Unit,
) {
    val backStack = rememberNavBackStack(WelcomeNavKey)

    // Notify callback of destination changes.
    LaunchedEffect(backStack.lastOrNull()) {
        backStack.lastOrNull()?.let { currentDestination ->
            onDestinationChanged(currentDestination)
        }
    }

    NavDisplay(
        entryDecorators = listOf(
//            rememberSceneSetupNavEntryDecorator(),
//            rememberSavedStateNavEntryDecorator(),
        ),
        backStack = backStack,
        transitionSpec = {
            ContentTransform(
                fadeIn(animationSpec = tween(200)),
                fadeOut(animationSpec = tween(200)),
            )
        },
        onBack = {
            backStack.removeLastOrNull()
        },
        entryProvider = { key ->
            // Keep this list of navigation entries sorted alphabetical.
            when (key) {
                is GameOverNavKey -> NavEntry(key) {
                    GameOverScreenSpec.Content(
                        arguments = key,
                        navigateTo = { route ->
                            backStack.add(route)
                        },
                    )
                }

                is GuessLocationNavKey -> NavEntry(key) {
                    GuessLocationScreenSpec.Content(
                        arguments = key,
                        navigateTo = { route ->
                            backStack.add(route)
                        },
                    )
                }

                is StreetViewNavKey -> NavEntry(key) {
                    StreetViewScreenSpec.Content(
                        arguments = key,
                        navigateTo = { route ->
                            backStack.add(route)
                        },
                    )
                }

                is WelcomeNavKey -> NavEntry(key) {
                    WelcomeScreenSpec.Content(
                        arguments = key,
                        navigateTo = { route ->
                            backStack.add(route)
                        },
                    )
                }

                else -> throw IllegalArgumentException("Unknown route: $key")
            }
        },
    )
}
