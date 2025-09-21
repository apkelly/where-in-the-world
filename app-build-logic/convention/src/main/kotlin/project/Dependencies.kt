package project

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.repositories

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureDependencies(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        repositories {
//            google()
//            mavenCentral()
//            maven { url = uri ("https://jitpack.io") }
//            maven { url = uri ("https://plugins.gradle.org/m2/") }
//            maven {
//                // Only used for Paparazzi snapshot releases
//                url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
//            }
        }
    }
}
