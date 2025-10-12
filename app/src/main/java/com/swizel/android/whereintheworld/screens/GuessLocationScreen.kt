package com.swizel.android.whereintheworld.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
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
    val dummy: String
)

@Composable
internal fun GuessLocationScreen(
    uiState: UiState<GuessLocationUiState>,
    isExpandedWidth: Boolean,
    onAction: (GuessLocationViewModel.Action) -> Unit,
) {
    BasicScaffold(
        uiState = uiState,
    ) { data ->
        Text("Guess Location Screen")

        val singapore = LatLng(1.35, 103.87)
        val singaporeMarkerState = rememberUpdatedMarkerState(position = singapore)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = singaporeMarkerState,
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }

    }
}

@Preview
@Composable
private fun GuessLocationScreenPreview() {
    WhereInTheWorldTheme {
        GuessLocationScreen(
            uiState = UiState(
                isLoading = LoadingType.NOT_LOADING,
                data = GuessLocationUiState(dummy = "dummy")
            ),
            isExpandedWidth = false,
            onAction = { }
        )
    }
}