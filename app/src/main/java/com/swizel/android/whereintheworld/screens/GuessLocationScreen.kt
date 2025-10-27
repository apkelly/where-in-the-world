package com.swizel.android.whereintheworld.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.swizel.android.whereintheworld.composables.BasicScaffold
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.theme.WhereInTheWorldTheme
import com.swizel.android.whereintheworld.viewmodels.GuessLocationViewModel


@Immutable
internal data class GuessLocationUiState(
    val numRounds: Int,
    val currentRound: Int,
)

private val MAP_CENTER = LatLng(25.0, 0.0)
private val TUTORIAL_PIN = LatLng(15.0, 0.0)

@Composable
internal fun GuessLocationScreen(
    uiState: UiState<GuessLocationUiState>,
    isExpandedWidth: Boolean,
    onAction: (GuessLocationViewModel.Action) -> Unit,
) {
    BasicScaffold(
        uiState = uiState,
    ) { data ->
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(MAP_CENTER, 2f)
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
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings,
            onMapLongClick = { location ->
                onAction(GuessLocationViewModel.Action.GuessLocation(location = location, 100L))
            }
        )

    }
}

@Preview
@Composable
private fun GuessLocationScreenPreview() {
    WhereInTheWorldTheme {
        GuessLocationScreen(
            uiState = UiState(
                isLoading = LoadingType.NOT_LOADING,
                data = GuessLocationUiState(
                    numRounds = 5,
                    currentRound = 1
                )
            ),
            isExpandedWidth = false,
            onAction = { }
        )
    }
}