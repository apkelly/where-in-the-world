package com.swizel.android.whereintheworld.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
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
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.swizel.android.whereintheworld.BuildConfig
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.composables.AppButton
import com.swizel.android.whereintheworld.composables.BasicScaffold
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.model.GameRoundResult
import com.swizel.android.whereintheworld.theme.WhereInTheWorldTheme
import com.swizel.android.whereintheworld.viewmodels.GameOverViewModel

@Immutable
internal data class GameOverUiState(
    val roundResults: List<GameRoundResult>,
    val score: Long,
    val signedInToGooglePlay: Boolean,
)

private const val SCORE_CARD_SIZE_PERCENT = 0.3f

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
        val appColors = WhereInTheWorldTheme.appColors
        val mapProperties = remember {
            MapProperties(
                maxZoomPreference = 15f,
                minZoomPreference = 2f,
                mapType = MapType.SATELLITE,
            )
        }

        val mapUiSettings = remember {
            MapUiSettings(
                mapToolbarEnabled = false,
                tiltGesturesEnabled = false,
                rotationGesturesEnabled = false,
                compassEnabled = false,
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true,
                myLocationButtonEnabled = false,
            )
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val overlayHeight = maxHeight * SCORE_CARD_SIZE_PERCENT

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
                data.roundResults.forEachIndexed { index, roundResult ->
                    val roundNumber = index + 1
                    val round = roundResult.round
                    val guess = roundResult.guess
                    // Always show the actual location pin.
                    val locationPinConfig = PinConfig.builder()
                        .setBackgroundColor(appColors.actualLocationPin.toArgb())
                        .build()

                    AdvancedMarker(
                        state = rememberUpdatedMarkerState(position = round.panoramaLatLng),
                        title = stringResource(id = R.string.actual_location_marker_title, roundNumber),
                        pinConfig = locationPinConfig,
                    )

                    guess?.let {
                        val guessPinConfig = PinConfig.builder()
                            .setBackgroundColor(appColors.guessLocationPin.toArgb())
                            .build()

                        AdvancedMarker(
                            state = rememberUpdatedMarkerState(position = guess.guessedLatLng),
                            title = stringResource(id = R.string.guess_location_marker_title, roundNumber),
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

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(SCORE_CARD_SIZE_PERCENT)
                    .align(Alignment.BottomCenter)
                    .safeContentPadding(),
                color = appColors.mapOverlayStrongContainer,
                contentColor = appColors.onMapOverlay,
            ) {
                // Semi-transparent overlay anchored to the bottom third of the screen.
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Text(
                        text = stringResource(id = R.string.score, data.score),
                        style = WhereInTheWorldTheme.typography.headlineLarge,
                    )

                    AppButton(
                        onClick = { onAction(GameOverViewModel.Action.PlayAgain) },
                        modifier = Modifier.fillMaxWidth(),
                        content = {
                            Text(text = stringResource(id = R.string.play_again))
                        },
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        val activity = LocalActivity.current
                        AppButton(
                            onClick = {
                                activity?.let {
                                    onAction(GameOverViewModel.Action.Leaderboards(activity = it))
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = data.signedInToGooglePlay,
                            content = {
                                Text(text = stringResource(id = R.string.leaderboards))
                            },
                        )

                        AppButton(
                            onClick = {
                                activity?.let {
                                    onAction(GameOverViewModel.Action.Achievements(activity = it))
                                }
                            },
                            modifier = Modifier.weight(1f),
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
}

@Preview
@Composable
private fun GameOverScreenPreview() {
    WhereInTheWorldTheme {
        GameOverScreen(
            uiState = UiState(
                isLoading = LoadingType.NOT_LOADING,
                data = GameOverUiState(
                    roundResults = emptyList(),
                    score = 0,
                    signedInToGooglePlay = true,
                ),
            ),
            isExpandedWidth = false,
            onAction = { },
        )
    }
}
