package com.swizel.android.whereintheworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.swizel.android.whereintheworld.navigation.NavigationHost
import com.swizel.android.whereintheworld.theme.WhereInTheWorldTheme
import com.swizel.android.whereintheworld.utils.Diagnostics
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val diagnostics: Diagnostics by inject()

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhereInTheWorldTheme {
                NavigationHost(
                    onDestinationChanged = { destination ->
                        diagnostics.trackNavigation(route = destination.toString())
                    },
                )
            }
        }
    }
}
