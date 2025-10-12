package com.swizel.android.whereintheworld.navigation

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.swizel.android.whereintheworld.screens.WelcomeScreen
import com.swizel.android.whereintheworld.theme.LocalWindowSizeClass
import com.swizel.android.whereintheworld.theme.isExpandedWidth
import com.swizel.android.whereintheworld.viewmodels.WelcomeViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
internal data object WelcomeNavKey : NavKey

internal object WelcomeScreenSpec : ScreenSpec<WelcomeNavKey>() {

    @Composable
    override fun Content(
        arguments: WelcomeNavKey,
        navigateTo: (NavKey) -> Unit,
    ) {
        val viewModel: WelcomeViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val activity = LocalActivity.current

        LaunchedEffect(Unit) {
            activity?.let {
                viewModel.fetchUiState(it)
            }
        }

        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
                // We don't care about the result.
            }

        WelcomeScreen(
            uiState = uiState,
            isExpandedWidth = LocalWindowSizeClass.current.isExpandedWidth(),
            onAction = { action ->
                viewModel.onAction(
                    action = action,
                    navigateTo = { route ->
                        navigateTo(route)
                    },
                    launchIntent = { intent ->
                        launcher.launch(intent)
                    }
                )
            },
        )
    }
}