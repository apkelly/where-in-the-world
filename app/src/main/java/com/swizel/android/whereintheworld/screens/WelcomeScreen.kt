package com.swizel.android.whereintheworld.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.composables.BasicScaffold
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.theme.WhereInTheWorldTheme
import com.swizel.android.whereintheworld.viewmodels.WelcomeViewModel
import kotlinx.coroutines.delay

@Immutable
internal data class WelcomeUiState(
    val signedInToGooglePlay: Boolean,
)

@Composable
internal fun WelcomeScreen(
    uiState: UiState<WelcomeUiState>,
    isExpandedWidth: Boolean,
    onAction: (WelcomeViewModel.Action) -> Unit,
) {
    val backgroundDrawables = if (isExpandedWidth) {
        listOf(
            R.drawable.welcome_rushmore,
            R.drawable.welcome_taj_mahal,
            R.drawable.welcome_pyramids,
        )
    } else {
        listOf(
            R.drawable.welcome_eiffel,
            R.drawable.welcome_pisa,
            R.drawable.welcome_christ,
        )
    }

    var currentImageIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            currentImageIndex = (currentImageIndex + 1) % backgroundDrawables.size
        }
    }

    BasicScaffold(
        uiState = uiState,
    ) { data ->
        Box(modifier = Modifier.fillMaxSize()) {
            Crossfade(
                targetState = currentImageIndex,
                animationSpec = tween(durationMillis = 1000),
            ) { index ->
                Image(
                    painter = painterResource(id = backgroundDrawables[index]),
                    contentDescription = stringResource(id = R.string.cd_welcome_background),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.7f)
                    .background(Color(0x66000000))
                    .padding(16.dp),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.game_mode),
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )

                    Button(
                        onClick = {
                            onAction(WelcomeViewModel.Action.SoloChallenge(gameDifficulty = GameDifficulty.EASY))
                        },
                        colors = ButtonDefaults.buttonColors().copy(disabledContentColor = Color.White, disabledContainerColor = Color.DarkGray),
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_action_user),
                                contentDescription = null,
                            )
                            Text(
                                text = stringResource(id = R.string.game_mode_solo),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f),
                            )
                        },
                    )

                    Button(
                        onClick = {
                            onAction(WelcomeViewModel.Action.QuickChallenge(gameDifficulty = GameDifficulty.EASY))
                        },
                        colors = ButtonDefaults.buttonColors().copy(disabledContentColor = Color.White, disabledContainerColor = Color.DarkGray),
                        enabled = data.signedInToGooglePlay,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_action_users),
                                contentDescription = null,
                            )
                            Text(
                                text = stringResource(id = R.string.game_mode_quick_challenge),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f),
                            )
                        },
                    )

                    Button(
                        onClick = {
                            onAction(WelcomeViewModel.Action.FriendChallenge(gameDifficulty = GameDifficulty.EASY))
                        },
                        colors = ButtonDefaults.buttonColors().copy(disabledContentColor = Color.White, disabledContainerColor = Color.DarkGray),
                        enabled = data.signedInToGooglePlay,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_action_users),
                                contentDescription = null,
                            )
                            Text(
                                text = stringResource(id = R.string.game_mode_friend_challenge),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f),
                            )
                        },
                    )
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .safeContentPadding(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                val activity = LocalActivity.current
                Button(
                    onClick = {
                        activity?.let {
                            onAction(WelcomeViewModel.Action.Leaderboards(activity = it))
                        }
                    },
                    colors = ButtonDefaults.buttonColors().copy(disabledContentColor = Color.White, disabledContainerColor = Color.DarkGray),
                    enabled = data.signedInToGooglePlay,
                    content = {
                        Text(text = stringResource(id = R.string.leaderboards))
                    },
                )

                Button(
                    onClick = {
                        activity?.let {
                            onAction(WelcomeViewModel.Action.Achievements(activity = it))
                        }
                    },
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

@Preview
@Composable
private fun WelcomeScreenPreview() {
    WhereInTheWorldTheme {
        WelcomeScreen(
            uiState = UiState(
                isLoading = LoadingType.NOT_LOADING,
                data = WelcomeUiState(signedInToGooglePlay = true),
            ),
            isExpandedWidth = false,
            onAction = { },
        )
    }
}
