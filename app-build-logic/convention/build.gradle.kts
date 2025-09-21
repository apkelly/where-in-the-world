import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

dependencies {
    compileOnly(libs.build.gradle)
    compileOnly(libs.jetbrains.kotlin.android)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "com.swizel.android.plugins.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "com.swizel.android.plugins.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplicationFilename") {
            id = "com.swizel.android.plugins.android.application.filename"
            implementationClass = "AndroidApplicationFilenameConventionPlugin"
        }
        register("androidApplicationSigning") {
            id = "com.swizel.android.plugins.android.application.signing"
            implementationClass = "AndroidApplicationSigningConventionPlugin"
        }
        register("androidApplicationVersion") {
            id = "com.swizel.android.plugins.android.application.version"
            implementationClass = "AndroidApplicationVersionConventionPlugin"
        }
        register("androidLibrary") {
            id = "com.swizel.android.plugins.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "com.swizel.android.plugins.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibraryNamespace") {
            id = "com.swizel.android.plugins.android.library.namespace"
            implementationClass = "AndroidLibraryNamespaceConventionPlugin"
        }
        register("androidLibraryNavigation") {
            id = "com.swizel.android.plugins.android.library.navigation"
            implementationClass = "AndroidLibraryNavigationConventionPlugin"
        }
//        register("androidRoom") {
//            id = "com.swizel.android.plugins.android.room"
//            implementationClass = "AndroidRoomConventionPlugin"
//        }


    }
}

dependencies {
//    implementation(libs.androidx.room.gradlePlugin)
    implementation(libs.ksp.gradle.plugin)
    implementation(libs.spotless)
}