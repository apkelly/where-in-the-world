package com.swizel.android.whereintheworld.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PinConfig
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.swizel.android.whereintheworld.BuildConfig
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.composables.BasicScaffold
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.model.GameRound
import com.swizel.android.whereintheworld.theme.WhereInTheWorldTheme
import com.swizel.android.whereintheworld.viewmodels.GameOverViewModel

@Immutable
internal data class GameOverUiState(
    val gameRounds: List<GameRound>,
    val score: Long,
    val signedInToGooglePlay: Boolean,
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
                    mapType = MapType.SATELLITE,
                ),
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
                ),
            )
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val overlayHeight = maxHeight * 0.33f

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings,
                googleMapOptionsFactory = {
                    GoogleMapOptions().mapId(BuildConfig.MAP_ID)
                },
                contentPadding = PaddingValues(bottom = overlayHeight),
            ) {
                data.gameRounds.forEachIndexed { index, round ->
                    // Always show the actual location pin.
                    val locationPinConfig = PinConfig.builder()
                        .setBackgroundColor(android.graphics.Color.YELLOW)
                        .build()
                    AdvancedMarker(
                        state = MarkerState(position = round.panoramaLatLng),
                        title = "Location for round ${index + 1}",
                        pinConfig = locationPinConfig,
                    )

                    round.guess?.let { guess ->
                        val guessPinConfig = PinConfig.builder()
                            .setBackgroundColor(android.graphics.Color.MAGENTA)
                            .build()

                        AdvancedMarker(
                            state = MarkerState(position = guess.guessedLatLng),
                            title = "Guess for round ${index + 1}",
                            pinConfig = guessPinConfig,
                        )

                        // Join the guess and the original location together.
                        val resources = LocalResources.current
                        Polyline(
                            points = listOf(round.panoramaLatLng, guess.guessedLatLng),
                            width = resources.getDimensionPixelSize(R.dimen.game_over_line_width).toFloat(),
                        )
                    }
                }
            }

            // Semi-transparent overlay anchored to the bottom third of the screen.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(0.33f)
                    .align(Alignment.BottomCenter)
                    .background(Color(0xCC000000))
                    .padding(16.dp)
                    .safeContentPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = "Score : ${data.score}",
                    style = WhereInTheWorldTheme.typography.headlineLarge,
                    color = Color.White,
                )

                Button(
                    onClick = {
                        onAction(GameOverViewModel.Action.PlayAgain)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors().copy(disabledContentColor = Color.White, disabledContainerColor = Color.DarkGray),
                    content = {
                        Text(text = stringResource(id = R.string.play_again))
                    },
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    val activity = LocalActivity.current
                    Button(
                        onClick = {
                            activity?.let {
                                onAction(GameOverViewModel.Action.Leaderboards(activity = it))
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors().copy(disabledContentColor = Color.White, disabledContainerColor = Color.DarkGray),
                        enabled = data.signedInToGooglePlay,
                        content = {
                            Text(text = stringResource(id = R.string.leaderboards))
                        },
                    )

                    Button(
                        onClick = {
                            activity?.let {
                                onAction(GameOverViewModel.Action.Achievements(activity = it))
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors().copy(disabledContentColor = Color.White, disabledContainerColor = Color.DarkGray),
                        enabled = data.signedInToGooglePlay,
                        content = {
                            Text(text = stringResource(id = R.string.achievements))
                        },
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
                    gameRounds = emptyList(),
                    score = 0,
                    signedInToGooglePlay = true,
                ),
            ),
            isExpandedWidth = false,
            onAction = { },
        )
    }
}
