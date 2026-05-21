package com.swizel.android.whereintheworld.screens

import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.StreetViewPanoramaOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.StreetViewSource
import com.google.maps.android.compose.streetview.StreetView
import com.google.maps.android.compose.streetview.rememberStreetViewCameraPositionState
import com.google.maps.android.ktx.MapsExperimentalFeature
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.composables.BasicScaffold
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.theme.WhereInTheWorldTheme
import com.swizel.android.whereintheworld.utils.ConsoleLogger
import com.swizel.android.whereintheworld.viewmodels.StreetViewViewModel
import java.util.concurrent.TimeUnit

@Immutable
internal data class StreetViewUiState(
    val numRounds: Int,
    val currentRound: Int,
    val timeAllowed: Long,
    val panoramaLatLng: LatLng,
    val landmark: String,
    val gameDifficulty: GameDifficulty,
)

@OptIn(MapsExperimentalFeature::class)
@Composable
internal fun StreetViewScreen(
    uiState: UiState<StreetViewUiState>,
    isExpandedWidth: Boolean,
    onAction: (StreetViewViewModel.Action) -> Unit,
) {
    BasicScaffold(
        uiState = uiState,
    ) { data ->
        LaunchedEffect(Unit) {
            ConsoleLogger.d("Current Round: ${data.currentRound}, Landmark: ${data.landmark}, LatLng: ${data.panoramaLatLng}")
        }
        var countdownTextColor by remember { mutableStateOf(Color.White) }
        var countdownText by remember { mutableStateOf("00:00.000") }
        var streetViewEnabled by remember { mutableStateOf(true) }

        val countdownTimer = remember(data.currentRound) {
            object : CountDownTimer(data.timeAllowed, 10) {
                private val halfTime = data.timeAllowed / 2
                private val quarterTime = data.timeAllowed / 4
                var lastTickMillis = 0L

                override fun onTick(
                    millis: Long,
                ) {
                    lastTickMillis = millis
                    var millisUntilFinished = millis

                    val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                    millisUntilFinished -= TimeUnit.SECONDS.toMillis(seconds)

                    countdownTextColor = when {
                        millis < quarterTime -> {
                            Color.Red
                        }
                        millis < halfTime -> {
                            Color.Yellow
                        }
                        else -> {
                            Color.White
                        }
                    }

                    countdownText = String.format("%02d:%02d.%03d", minutes, seconds, millisUntilFinished)
                }

                override fun onFinish() {
                    streetViewEnabled = false
                    lastTickMillis = 0L

                    countdownTextColor = Color.Red
                    countdownText = "00:00.000"
                }
            }
        }

        DisposableEffect(countdownTimer) {
            countdownTimer.start()
            onDispose {
                countdownTimer.cancel()
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            val cameraPositionState = rememberStreetViewCameraPositionState()
            // OUTDOOR + tiny radius can yield no panorama on some coordinates, this causes app crashes.
            val streetViewRadiusMeters = 200
            val streetViewSource = StreetViewSource.OUTDOOR

            LaunchedEffect(data.currentRound, data.panoramaLatLng) {
                cameraPositionState.setPosition(
                    data.panoramaLatLng,
                    streetViewRadiusMeters,
                    streetViewSource,
                )
            }

            StreetView(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                isStreetNamesEnabled = false,
                isPanningGesturesEnabled = streetViewEnabled,
                isUserNavigationEnabled = streetViewEnabled,
                isZoomGesturesEnabled = streetViewEnabled,
                streetViewPanoramaOptionsFactory = {
                    StreetViewPanoramaOptions().position(
                        data.panoramaLatLng,
                        streetViewRadiusMeters,
                        streetViewSource,
                    )
                },
            )

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x66000000))
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = countdownText,
                        style = WhereInTheWorldTheme.typography.headlineLarge,
                        modifier = Modifier
                            .systemBarsPadding(),
                        color = countdownTextColor,
                    )
                    Text(
                        text = "${data.currentRound + 1}/${data.numRounds}",
                        modifier = Modifier
                            .systemBarsPadding(),
                        style = WhereInTheWorldTheme.typography.headlineLarge,
                        color = Color.White,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp),
                ) {
                    Button(
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_action_map),
                                tint = Color.White,
                                contentDescription = null,
                            )
                            Text(
                                text = stringResource(id = R.string.btn_guess),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f),
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .safeContentPadding(),
                        onClick = {
                            onAction(StreetViewViewModel.Action.GuessLocation(data.timeAllowed - countdownTimer.lastTickMillis))
                        },
                    )

                    Button(
                        content = {
                            Icon(
                                imageVector = Icons.Filled.QuestionMark,
                                tint = Color.White,
                                contentDescription = null,
                            )
                            Text(
                                text = stringResource(id = R.string.btn_hint),
                                textAlign = TextAlign.Center,
                            )
                        },
                        enabled = data.gameDifficulty != GameDifficulty.EXTREME,
                        modifier = Modifier
                            .safeContentPadding(),
                        onClick = {
                            onAction(StreetViewViewModel.Action.HintRequested)
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun StreetViewScreenPreview() {
    WhereInTheWorldTheme {
        StreetViewScreen(
            uiState = UiState(
                isLoading = LoadingType.NOT_LOADING,
                data = StreetViewUiState(
                    numRounds = 5,
                    currentRound = 1,
                    timeAllowed = 50_000,
                    panoramaLatLng = LatLng(0.0, 0.0),
                    landmark = "Ocean",
                    gameDifficulty = GameDifficulty.EASY,
                ),
            ),
            isExpandedWidth = false,
            onAction = { },
        )
    }
}
