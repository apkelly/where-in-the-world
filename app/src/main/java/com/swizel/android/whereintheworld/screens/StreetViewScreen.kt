package com.swizel.android.whereintheworld.screens

import android.os.SystemClock
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import com.swizel.android.whereintheworld.composables.AppButton
import com.swizel.android.whereintheworld.composables.BasicScaffold
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.Hint
import com.swizel.android.whereintheworld.theme.WhereInTheWorldTheme
import com.swizel.android.whereintheworld.viewmodels.StreetViewViewModel
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

@Immutable
internal data class StreetViewUiState(
    val numRounds: Int,
    val currentRound: Int,
    val timeAllowed: Long,
    val panoramaLatLng: LatLng,
    val landmark: String,
    val country: String,
    val gameDifficulty: GameDifficulty,
    val currentHint: Hint,
)

private data class SelectedHint(
    val hint: Hint,
    val value: String,
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
        var countdownTextColor by remember(data.currentRound) { mutableStateOf(Color.White) }
        var countdownText by remember(data.currentRound) { mutableStateOf(formatCountdown(data.timeAllowed)) }
        var remainingMillis by remember(data.currentRound) { mutableLongStateOf(data.timeAllowed) }
        var streetViewEnabled by remember(data.currentRound) { mutableStateOf(true) }
        var showHintDialog by remember(data.currentRound) { mutableStateOf(false) }
        var revealedHints by remember(data.currentRound) { mutableStateOf(emptySet<Hint>()) }

        LaunchedEffect(data.currentRound, data.timeAllowed, showHintDialog) {
            var lastTickMillis = SystemClock.elapsedRealtime()
            while (remainingMillis > 0L) {
                delay(10.milliseconds)
                val now = SystemClock.elapsedRealtime()
                if (!showHintDialog) {
                    remainingMillis = (remainingMillis - (now - lastTickMillis)).coerceAtLeast(0L)
                }
                lastTickMillis = now
            }
        }

        LaunchedEffect(remainingMillis, data.timeAllowed) {
            val halfTime = data.timeAllowed / 2
            val quarterTime = data.timeAllowed / 4

            countdownTextColor = when {
                remainingMillis <= 0L -> {
                    Color.Red
                }
                remainingMillis < quarterTime -> {
                    Color.Red
                }
                remainingMillis < halfTime -> {
                    Color.Yellow
                }
                else -> {
                    Color.White
                }
            }

            countdownText = formatCountdown(remainingMillis)
            streetViewEnabled = remainingMillis > 0L
        }

        LaunchedEffect(data.currentHint) {
            if (data.currentHint != Hint.NONE) {
                revealedHints = revealedHints + data.currentHint
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
                Column {
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

                    revealedHints
                        .mapNotNull { hint -> hint.toSelectedHint(data) }
                        .forEach { hint ->
                            Text(
                                text = stringResource(
                                    id = R.string.hint_revealed,
                                    hint.hint.label(),
                                    hint.value,
                                ),
                                style = WhereInTheWorldTheme.typography.bodyLarge,
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0x99000000))
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                            )
                        }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp),
                ) {
                    AppButton(
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
                            onAction(StreetViewViewModel.Action.GuessLocation(data.timeAllowed - remainingMillis))
                        },
                    )

                    AppButton(
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
                            showHintDialog = true
                        },
                    )
                }
            }

            if (showHintDialog) {
                HintSelectionDialog(
                    onDismissRequest = {
                        showHintDialog = false
                    },
                    onHintSelected = { hint ->
                        showHintDialog = false
                        revealedHints = revealedHints + hint
                        onAction(StreetViewViewModel.Action.HintRequested(hint = hint))
                    },
                )
            }
        }
    }
}

@Composable
private fun HintSelectionDialog(
    onDismissRequest: () -> Unit,
    onHintSelected: (Hint) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(id = R.string.hint_dialog_title))
        },
        text = {
            Column {
                Text(text = stringResource(id = R.string.hint_dialog_message))

                Hint.entries.filter { it != Hint.NONE }.forEach { hint ->
                    TextButton(
                        onClick = {
                            onHintSelected(hint)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.hint_option,
                                hint.label(),
                                hint.multiplierText(),
                            ),
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
    )
}

private fun Hint.toSelectedHint(
    data: StreetViewUiState,
): SelectedHint? = when (this) {
    Hint.NONE -> null
    Hint.COUNTRY -> SelectedHint(hint = this, value = data.country)
    Hint.LANDMARK -> SelectedHint(hint = this, value = data.landmark)
}

@Composable
private fun Hint.label(): String = when (this) {
    Hint.NONE -> stringResource(id = R.string.hint_none)
    Hint.COUNTRY -> stringResource(id = R.string.hint_country)
    Hint.LANDMARK -> stringResource(id = R.string.hint_landmark)
}

private fun Hint.multiplierText(): String = String.format(Locale.US, "x%.2f", multiplier)

private fun formatCountdown(
    remainingMillis: Long,
): String {
    var millisUntilFinished = remainingMillis
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
    millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
    millisUntilFinished -= TimeUnit.SECONDS.toMillis(seconds)
    return String.format(Locale.US, "%02d:%02d.%03d", minutes, seconds, millisUntilFinished)
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
                    country = "Atlantis",
                    gameDifficulty = GameDifficulty.EASY,
                    currentHint = Hint.NONE,
                ),
            ),
            isExpandedWidth = false,
            onAction = { },
        )
    }
}
