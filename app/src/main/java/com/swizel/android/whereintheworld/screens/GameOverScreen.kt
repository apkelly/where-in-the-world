package com.swizel.android.whereintheworld.screens

import android.graphics.Color
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PinConfig
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.swizel.android.whereintheworld.composables.BasicScaffold
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.model.Guess
import com.swizel.android.whereintheworld.viewmodels.GameOverViewModel


@Immutable
internal data class GameOverUiState(
    val guesses: List<Guess>
)

@Composable
internal fun GameOverScreen(
    uiState: UiState<GameOverUiState>,
    isExpandedWidth: Boolean,
    onAction: (GameOverViewModel.Action) -> Unit,
) {
    BasicScaffold(
        uiState = uiState,
    ) { data ->
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 2f)
        }
        var mapProperties by remember {
            mutableStateOf(
                MapProperties(
                    maxZoomPreference = 15f,
                    minZoomPreference = 2f,
                    mapType = MapType.SATELLITE
                )
            )
        }

        var mapUiSettings by remember {
            mutableStateOf(
                MapUiSettings(
                    mapToolbarEnabled = false,
                    tiltGesturesEnabled = false,
                    rotationGesturesEnabled = false,
                    compassEnabled = false,
                    zoomControlsEnabled = true,
                    zoomGesturesEnabled = true,
                    myLocationButtonEnabled = false,
                )
            )
        }

        GoogleMap(
            modifier = Modifier.fillMaxWidth().aspectRatio(0.5f),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings,
        ) {
            data.guesses.forEachIndexed { index, guess ->
                // A guessed LatLng will be missing if the user didn't make a guess quick enough.
                guess.guessedLatLng?.let { guessedLocation ->
                    val pinConfig = PinConfig.builder()
                        .setBackgroundColor(Color.MAGENTA)
                        .build()

                    AdvancedMarker(
                        state = MarkerState(position = guessedLocation),
                        title = "Guess for round ${index + 1}",
                        pinConfig = pinConfig
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GameOverScreenPreview() {
    MaterialTheme {
        GameOverScreen(
            uiState = UiState(
                isLoading = LoadingType.NOT_LOADING,
                data = GameOverUiState(
                    guesses = emptyList()
                )
            ),
            isExpandedWidth = false,
            onAction = { }
        )
    }
}