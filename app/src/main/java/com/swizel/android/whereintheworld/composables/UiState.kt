package com.swizel.android.whereintheworld.composables

data class UiState<out SuccessData>(
    val isLoading: LoadingType = LoadingType.NOT_LOADING,
    val fatalError: Throwable? = null,
    val data: SuccessData? = null,
)

enum class LoadingType(
    val showSpinner: Boolean,
    val message: String? = null,
) {
    NOT_LOADING(false, null),
    SPINNER_ONLY(true, null),
    PROCESSING(true, "Processing"),
    LOADING(true, "Loading..."),
}
