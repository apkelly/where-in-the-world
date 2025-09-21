package project

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = commonLibs.findVersion("compileSdk").get().toString().toInt()
        defaultConfig {
            minSdk = commonLibs.findVersion("minSdk").get().toString().toInt()
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            isCoreLibraryDesugaringEnabled = true
        }

        buildFeatures.buildConfig = true
    }

    extensions.configure<KotlinProjectExtension> {
        jvmToolchain(JvmTarget.JVM_17.target.toInt())
    }

    dependencies {
        add("coreLibraryDesugaring", commonLibs.findLibrary("core-library-desugaring").get())
    }
}
