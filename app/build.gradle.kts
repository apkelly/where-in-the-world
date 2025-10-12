plugins {
    alias(libs.plugins.app.android.application)
    alias(libs.plugins.app.android.application.compose)
    alias(libs.plugins.app.android.application.filename)
    alias(libs.plugins.app.android.application.signing)
    alias(libs.plugins.app.android.application.versions)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.performance)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
}

android {
    namespace = "com.swizel.android.whereintheworld"

    defaultConfig {
        applicationId = "com.swizel.android.whereintheworld"
    }

    androidResources {
        ignoreAssetsPattern = "*.sql"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // AndroidX
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.jakewharton.timber)

    // Google Play Games and Maps
    implementation(libs.play.services.auth)
    implementation(libs.play.services.gamesv2)
    implementation(libs.play.services.maps)

    // Firebase
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.analytics)
    implementation(libs.google.firebase.crashlytics)
    implementation(libs.google.firebase.performance)
    implementation(libs.google.firebase.remoteconfig)
}
