package com.swizel.android.whereintheworld.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swizel.android.whereintheworld.theme.WhereInTheWorldTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UiStateLoading(
    loadingType: LoadingType = LoadingType.SPINNER_ONLY,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = WhereInTheWorldTheme.colorScheme.primaryContainer,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (loadingType.showSpinner) {
                LoadingIndicator(
                    modifier = Modifier.size(96.dp),
                    color = WhereInTheWorldTheme.colorScheme.onPrimary,
                )
            }
            loadingType.message?.let { message ->
                Text(
                    text = message,
                    color = WhereInTheWorldTheme.colorScheme.onPrimary,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                )
            }
        }
    }
}
