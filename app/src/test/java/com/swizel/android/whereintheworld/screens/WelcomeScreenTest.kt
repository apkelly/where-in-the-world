package com.swizel.android.whereintheworld.screens

import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.test.BasePaparazziTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class WelcomeScreenTest(
    @TestParameter config: Config,
) : BasePaparazziTest(config) {

    @Test
    fun welcomeScreenSignedInSnapshot() {
        paparazziSnapshot {
            WelcomeScreen(
                uiState = UiState(
                    isLoading = LoadingType.NOT_LOADING,
                    data = WelcomeUiState(signedInToGooglePlay = true),
                ),
                isExpandedWidth = false,
                onAction = { },
            )
        }
    }

    @Test
    fun welcomeScreenSignedOutSnapshot() {
        paparazziSnapshot {
            WelcomeScreen(
                uiState = UiState(
                    isLoading = LoadingType.NOT_LOADING,
                    data = WelcomeUiState(signedInToGooglePlay = false),
                ),
                isExpandedWidth = false,
                onAction = { },
            )
        }
    }

    @Test
    fun welcomeScreenLoadingSnapshot() {
        paparazziSnapshot {
            WelcomeScreen(
                uiState = UiState(
                    isLoading = LoadingType.LOADING,
                ),
                isExpandedWidth = false,
                onAction = { },
            )
        }
    }

}
