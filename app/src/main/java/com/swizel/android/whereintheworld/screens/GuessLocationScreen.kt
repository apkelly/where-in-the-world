package com.swizel.android.whereintheworld.screens

import android.content.Context
import android.graphics.Canvas
import android.view.animation.BounceInterpolator
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.swizel.android.whereintheworld.BuildConfig
import com.swizel.android.whereintheworld.Config
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.composables.BasicScaffold
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.theme.WhereInTheWorldTheme
import com.swizel.android.whereintheworld.utils.SettingsUtils
import com.swizel.android.whereintheworld.viewmodels.GuessLocationViewModel
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

@Immutable
internal data class GuessLocationUiState(
    val numRounds: Int,
    val currentRound: Int,
)

private val MAP_CENTER = LatLng(25.0, 0.0)
private val TUTORIAL_PIN = LatLng(15.0, 0.0)

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
    ) { _ ->
        val context = LocalContext.current
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(MAP_CENTER, 2f)
        }
        var nextPinAnimationKey by remember { mutableLongStateOf(0L) }
        var droppedPin by remember {
            mutableStateOf(
                if (SettingsUtils.getBooleanPreference(context, SettingsUtils.FIRST_EVER_GUESS, true)) {
                    GuessPin(
                        position = TUTORIAL_PIN,
                        isTutorial = true,
                        animationKey = nextPinAnimationKey,
                    )
                } else {
                    null
                },
            )
        }
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

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings,
            googleMapOptionsFactory = {
                GoogleMapOptions().mapId(BuildConfig.MAP_ID)
            },
            contentPadding = PaddingValues(vertical = 48.dp), // Google branding & Zoom controls.
            onMapLongClick = { location ->
                SettingsUtils.addPreference(context, SettingsUtils.FIRST_EVER_GUESS, false)
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
                val snippet = stringResource(
                    id = if (pin.isTutorial) {
                        R.string.tutorial_snippet
                    } else {
                        R.string.confirm_guess
                    },
                )

                LaunchedEffect(pin.animationKey) {
                    if (pin.isTutorial) {
                        markerAnchorY.snapTo(Config.MAP_GUESS_V_ANCHOR)
                    } else {
                        markerAnchorY.snapTo(Config.MAP_GUESS_V_ANCHOR * 5)
                        markerAnchorY.animateTo(
                            targetValue = Config.MAP_GUESS_V_ANCHOR,
                            animationSpec = tween(
                                durationMillis = Config.MAPPING_DROPPED_PIN_ANIMATION_SPEED_MS.toInt(),
                                easing = { fraction -> BounceInterpolator().getInterpolation(fraction) },
                            ),
                        )
                        delay(250.milliseconds)
                    }

                    markerState.showInfoWindow()
                }

                MarkerInfoWindowContent(
                    state = markerState,
                    anchor = Offset(Config.MAP_GUESS_H_ANCHOR, markerAnchorY.value),
                    icon = markerIcon,
                    infoWindowAnchor = Offset(Config.MAP_INFO_H_ANCHOR, Config.MAP_INFO_V_ANCHOR),
                    onClick = { marker ->
                        marker.showInfoWindow()
                        true
                    },
                    onInfoWindowClick = {
                        if (!pin.isTutorial) {
                            onAction(GuessLocationViewModel.Action.GuessLocation(location = pin.position))
                        }
                    },
                ) {
                    Text(
                        text = snippet,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }
        }
    }
}

private fun bitmapDescriptorFromVectorDrawable(
    context: Context,
    @DrawableRes drawableId: Int,
): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context, drawableId) ?: return null
    val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return runCatching {
        MapsInitializer.initialize(context.applicationContext)
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }.getOrNull()
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
                ),
            ),
            isExpandedWidth = false,
            onAction = { },
        )
    }
}
