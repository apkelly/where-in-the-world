package com.swizel.android.whereintheworld.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.swizel.android.whereintheworld.theme.WhereInTheWorldTheme
import kotlinx.coroutines.delay

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun <T> BasicScaffold(
    uiState: UiState<T>,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    backgroundColor: Color = WhereInTheWorldTheme.colorScheme.background,
    loadingState: @Composable ColumnScope.() -> Unit = {
        var showLoading by remember { mutableStateOf(false) }
        LaunchedEffect(uiState) {
            // Don't display spinner for short loading durations.
            delay(500)
            showLoading = true
        }
        if (showLoading) {
            UiStateLoading()
        }
    },
    errorState: @Composable (Throwable) -> Unit = { data ->
        UiStateError(
            message = data.message,
        )
    },
    successState: @Composable ColumnScope.(T) -> Unit = { },
    bottomBar: @Composable (T) -> Unit = { },
) {
    Scaffold(
        topBar = { },
        containerColor = backgroundColor,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            uiState.data?.let { data ->
                bottomBar(data)
            }
        },
        modifier = Modifier
            .navigationBarsPadding(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor),
        ) {
            if (uiState.isLoading != LoadingType.NOT_LOADING) {
                loadingState()
            } else if (uiState.fatalError != null) {
                errorState(uiState.fatalError)
            } else {
                successState(uiState.data!!)
            }
        }
    }
}