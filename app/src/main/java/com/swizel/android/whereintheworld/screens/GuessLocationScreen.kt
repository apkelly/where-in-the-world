package com.swizel.android.whereintheworld.screens

import android.view.animation.BounceInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.swizel.android.whereintheworld.BuildConfig
import com.swizel.android.whereintheworld.Config
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.composables.AppButton
import com.swizel.android.whereintheworld.composables.BasicScaffold
import com.swizel.android.whereintheworld.composables.HintSelectionDialog
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.composables.label
import com.swizel.android.whereintheworld.composables.toSelectedHint
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.Hint
import com.swizel.android.whereintheworld.theme.WhereInTheWorldTheme
import com.swizel.android.whereintheworld.utils.SettingsUtils
import com.swizel.android.whereintheworld.utils.bitmapDescriptorFromVectorDrawable
import com.swizel.android.whereintheworld.viewmodels.GuessLocationViewModel
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

@Immutable
internal data class GuessLocationUiState(
    val numRounds: Int,
    val currentRound: Int,
    val landmark: String,
    val country: String,
    val gameDifficulty: GameDifficulty,
    val currentHint: Hint,
    val revealedHints: List<Hint>,
)

private val MAP_CENTER = LatLng(25.0, 0.0)
private val TUTORIAL_PIN = LatLng(15.0, 0.0)
private val TUTORIAL_REMINDER_DELAY = 30.seconds

private data class GuessPin(
    val position: LatLng,
    val isTutorial: Boolean,
    val animationKey: Long,
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
        val context = LocalContext.current
        val appColors = WhereInTheWorldTheme.appColors
        val shouldShowTutorialInitially = remember(data.currentRound) {
            SettingsUtils.getBooleanPreference(context, SettingsUtils.FIRST_EVER_GUESS, true)
        }
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(MAP_CENTER, 2f)
        }
        var isMapLoaded by remember(data.currentRound) { mutableStateOf(false) }
        var nextPinAnimationKey by remember(data.currentRound) { mutableLongStateOf(0L) }
        var hasSeededTutorialPin by remember(data.currentRound) { mutableStateOf(!shouldShowTutorialInitially) }
        var showTutorialOverlay by remember(data.currentRound) { mutableStateOf(shouldShowTutorialInitially) }
        var showHintDialog by remember(data.currentRound) { mutableStateOf(false) }
        var droppedPin by remember(data.currentRound) { mutableStateOf<GuessPin?>(null) }
        val markerIcon = remember(context) {
            bitmapDescriptorFromVectorDrawable(context, R.drawable.ic_action_pin)
        }
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

        fun showTutorialPromptWithPin() {
            showTutorialOverlay = true
            val nextAnimationKey = nextPinAnimationKey + 1
            nextPinAnimationKey = nextAnimationKey
            droppedPin = GuessPin(
                position = TUTORIAL_PIN,
                isTutorial = true,
                animationKey = nextAnimationKey,
            )
            hasSeededTutorialPin = true
        }

        LaunchedEffect(isMapLoaded, hasSeededTutorialPin, shouldShowTutorialInitially) {
            if (!isMapLoaded || hasSeededTutorialPin || !shouldShowTutorialInitially) {
                return@LaunchedEffect
            }
            showTutorialPromptWithPin()
        }

        LaunchedEffect(isMapLoaded, droppedPin?.isTutorial, showTutorialOverlay) {
            if (!isMapLoaded || showTutorialOverlay || droppedPin?.isTutorial == false) {
                return@LaunchedEffect
            }
            if (droppedPin == null) {
                delay(TUTORIAL_REMINDER_DELAY)
                if (droppedPin == null) {
                    showTutorialPromptWithPin()
                }
            }
        }

        LaunchedEffect(isMapLoaded, showTutorialOverlay, droppedPin?.isTutorial) {
            if (!isMapLoaded || !showTutorialOverlay || droppedPin?.isTutorial == true) {
                return@LaunchedEffect
            }
            showTutorialPromptWithPin()
        }

        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings,
                googleMapOptionsFactory = {
                    GoogleMapOptions().mapId(BuildConfig.MAP_ID)
                },
                contentPadding = PaddingValues(vertical = 48.dp), // Google branding & Zoom controls.
                onMapLoaded = {
                    isMapLoaded = true
                },
                onMapLongClick = { location ->
                    SettingsUtils.addPreference(context, SettingsUtils.FIRST_EVER_GUESS, false)
                    showTutorialOverlay = false
                    nextPinAnimationKey += 1
                    droppedPin = GuessPin(
                        position = location,
                        isTutorial = false,
                        animationKey = nextPinAnimationKey,
                    )
                },
            ) {
                droppedPin?.let { pin ->
                    val markerState = rememberUpdatedMarkerState(position = pin.position)
                    val markerAnchorY = remember { Animatable(Config.MAP_GUESS_V_ANCHOR) }
                    val infoWindowText = stringResource(
                        id = if (pin.isTutorial) {
                            R.string.tutorial_snippet
                        } else {
                            R.string.confirm_guess
                        },
                    )

                    LaunchedEffect(pin.animationKey, isMapLoaded) {
                        if (!isMapLoaded) {
                            return@LaunchedEffect
                        }
                        markerAnchorY.snapTo(Config.MAP_GUESS_V_ANCHOR * 5)
                        markerAnchorY.animateTo(
                            targetValue = Config.MAP_GUESS_V_ANCHOR,
                            animationSpec = tween(
                                durationMillis = Config.MAPPING_DROPPED_PIN_ANIMATION_SPEED_MS.toInt(),
                                easing = { fraction -> BounceInterpolator().getInterpolation(fraction) },
                            )
                        )
                        if (!pin.isTutorial) {
                            delay(250.milliseconds)
                            markerState.showInfoWindow()
                        }
                    }

                    Marker(
                        state = markerState,
                        anchor = Offset(Config.MAP_GUESS_H_ANCHOR, markerAnchorY.value),
                        icon = markerIcon,
                        infoWindowAnchor = Offset(Config.MAP_INFO_H_ANCHOR, Config.MAP_INFO_V_ANCHOR),
                        title = if (pin.isTutorial) null else infoWindowText,
                        onClick = { marker ->
                            if (!pin.isTutorial) {
                                marker.showInfoWindow()
                            }
                            true
                        },
                        onInfoWindowClick = {
                            if (!pin.isTutorial) {
                                onAction(GuessLocationViewModel.Action.GuessLocation(location = pin.position))
                            }
                        },
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .safeContentPadding(),
                ) {
                    if (showTutorialOverlay) {
                        Surface(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = appColors.mapOverlayStrongContainer,
                            contentColor = appColors.onMapOverlay,
                        ) {
                            Text(
                                text = stringResource(id = R.string.tutorial_snippet),
                                style = WhereInTheWorldTheme.typography.bodyLarge,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            )
                        }
                    }

                    data.revealedHints
                        .mapNotNull { hint -> hint.toSelectedHint(country = data.country, landmark = data.landmark) }
                        .forEach { hint ->
                            Text(
                                text = stringResource(
                                    id = R.string.hint_revealed,
                                    hint.hint.label(),
                                    hint.value,
                                ),
                                style = WhereInTheWorldTheme.typography.bodyLarge,
                                color = appColors.onMapOverlay,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .background(appColors.mapOverlayStrongContainer)
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                            )
                        }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .safeContentPadding(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                ) {
                    AppButton(
                        content = {
                            Icon(
                                imageVector = Icons.Filled.QuestionMark,
                                contentDescription = null,
                            )
                            Text(
                                text = stringResource(id = R.string.btn_hint),
                                textAlign = TextAlign.Center,
                            )
                        },
                        enabled = data.gameDifficulty != GameDifficulty.EXTREME,
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
                        onAction(GuessLocationViewModel.Action.HintRequested(hint = hint))
                    },
                )
            }
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
                data = GuessLocationUiState(
                    numRounds = 5,
                    currentRound = 1,
                    landmark = "Ocean",
                    country = "Atlantis",
                    gameDifficulty = GameDifficulty.EASY,
                    currentHint = Hint.NONE,
                    revealedHints = emptyList(),
                ),
            ),
            isExpandedWidth = false,
            onAction = { },
        )
    }
}
