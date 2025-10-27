package com.swizel.android.whereintheworld.screens

import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.StreetViewPanoramaOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.StreetViewSource
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.google.maps.android.compose.streetview.StreetView
import com.google.maps.android.compose.streetview.StreetViewCameraPositionState
import com.google.maps.android.compose.streetview.rememberStreetViewCameraPositionState
import com.google.maps.android.ktx.MapsExperimentalFeature
import com.swizel.android.whereintheworld.composables.BasicScaffold
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.theme.WhereInTheWorldTheme
import com.swizel.android.whereintheworld.viewmodels.StreetViewViewModel
import java.util.concurrent.TimeUnit


@Immutable
internal data class StreetViewUiState(
    val numRounds: Int,
    val currentRound: Int,
    val panoramaLatLng: LatLng,
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
        val countdownTimer = remember {
            object : CountDownTimer(50_000, 10) {
                override fun onTick(millis: Long) {
                    var millisUntilFinished = millis
//                    countdownTimerStarted = true
//
//                    viewModel.remainingStreetViewTimer = millisUntilFinished
//
//                    setTimerColor(millisUntilFinished)

                    val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                    millisUntilFinished -= TimeUnit.SECONDS.toMillis(seconds)

//                    countdown_timer.text = String.format("%02d:%02d.%03d", minutes, seconds, millisUntilFinished)
                }

                override fun onFinish() {
//                    toggleStreetViewEnabled(false)
//                    setTimerColor(0)
//                    countdown_timer.text = "00:00.000"
                }
            }
        }

        LaunchedEffect(countdownTimer) {
            countdownTimer.start()
        }

        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            StreetView(
                modifier = Modifier.fillMaxSize(),
                isStreetNamesEnabled = false,
                streetViewPanoramaOptionsFactory = {
                    StreetViewPanoramaOptions().position(data.panoramaLatLng, StreetViewSource.OUTDOOR)
                },
            )

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x66000000))
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "00:00.000",
                        style = WhereInTheWorldTheme.typography.headlineLarge,
                        modifier = Modifier
                            .systemBarsPadding(),
                        color = Color.White
                    )
                    Text(
                        text = "${data.currentRound + 1}/${data.numRounds}",
                        modifier = Modifier
                            .systemBarsPadding(),
                        style = WhereInTheWorldTheme.typography.headlineLarge,
                        color = Color.White
                    )
                }
                Button(
                    content = {
                        Text(
                            text = "Guess Location",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    },
                    onClick = {
                        onAction(StreetViewViewModel.Action.GuessLocation)
                    }
                )

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
                    panoramaLatLng = LatLng(0.0, 0.0)
                )
            ),
            isExpandedWidth = false,
            onAction = { }
        )
    }
}