package com.swizel.android.whereintheworld.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavKey
import com.swizel.android.whereintheworld.screens.GameOverUiState
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.screens.StreetViewUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class GameOverViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<UiState<GameOverUiState>>(UiState(isLoading = LoadingType.LOADING))
    val uiState = _uiState.asStateFlow()

    fun fetchUiState() {
        _uiState.value = UiState(
            isLoading = LoadingType.NOT_LOADING,
            data = GameOverUiState(
                dummy = "dummy"
            )
        )
    }

    sealed class Action {
    }

    fun onAction(
        action: Action,
        navigateTo: (NavKey) -> Unit,
    ) {
    }

}