package com.swizel.android.whereintheworld.startup

import android.content.Context
import com.github.apkelly.bolt.startup.BoltInitializer
import com.swizel.android.whereintheworld.BuildConfig
import com.swizel.android.whereintheworld.model.GameState
import com.swizel.android.whereintheworld.repositories.GameSessionRepository
import com.swizel.android.whereintheworld.usecases.CompleteGameUseCase
import com.swizel.android.whereintheworld.usecases.GetCurrentGameDifficultyUseCase
import com.swizel.android.whereintheworld.usecases.GetCurrentRoundProgressUseCase
import com.swizel.android.whereintheworld.usecases.GetCurrentRoundUseCase
import com.swizel.android.whereintheworld.usecases.NewGameUseCase
import com.swizel.android.whereintheworld.usecases.RecordStreetViewTimeUseCase
import com.swizel.android.whereintheworld.usecases.RequestHintUseCase
import com.swizel.android.whereintheworld.usecases.SubmitGuessUseCase
import com.swizel.android.whereintheworld.utils.ConsoleLogger
import com.swizel.android.whereintheworld.utils.Diagnostics
import com.swizel.android.whereintheworld.utils.GoogleClientHelper
import com.swizel.android.whereintheworld.utils.RemoteConfig
import com.swizel.android.whereintheworld.utils.impl.FirebaseDiagnostics
import com.swizel.android.whereintheworld.utils.impl.FirebaseRemoteConfig
import com.swizel.android.whereintheworld.viewmodels.GameOverViewModel
import com.swizel.android.whereintheworld.viewmodels.GuessLocationViewModel
import com.swizel.android.whereintheworld.viewmodels.StreetViewViewModel
import com.swizel.android.whereintheworld.viewmodels.WelcomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class KoinInitializer : BoltInitializer {
    override suspend fun create(
        context: Context,
    ) {
        withContext(Dispatchers.Default) {
            ConsoleLogger.d("KoinInitializer")

            // Register all our dependencies
            startKoin {
                androidContext(context.applicationContext)
                androidLogger()
                modules(
                    module {
                        viewModelOf(::GameOverViewModel)
                        viewModelOf(::GuessLocationViewModel)
                        viewModelOf(::StreetViewViewModel)
                        viewModelOf(::WelcomeViewModel)
                        single<Diagnostics> { FirebaseDiagnostics() }
                        single<RemoteConfig> { FirebaseRemoteConfig(BuildConfig.DEBUG) }
                        single { GoogleClientHelper(context) }
                        single { GameSessionRepository(GameState()) }
                        factoryOf(::CompleteGameUseCase)
                        factoryOf(::GetCurrentGameDifficultyUseCase)
                        factoryOf(::GetCurrentRoundProgressUseCase)
                        factoryOf(::GetCurrentRoundUseCase)
                        factoryOf(::NewGameUseCase)
                        factoryOf(::RecordStreetViewTimeUseCase)
                        factoryOf(::RequestHintUseCase)
                        factoryOf(::SubmitGuessUseCase)
                    },
                )
            }
        }
    }

    override fun dependencies(): List<Class<out BoltInitializer>> = listOf(LoggingInitializer::class.java)
}
