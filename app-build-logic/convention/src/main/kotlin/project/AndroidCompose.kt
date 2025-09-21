package project

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = commonLibs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("implementation", commonLibs.findLibrary("androidx-compose-ui-tooling-preview").get())
            add("debugImplementation", commonLibs.findLibrary("androidx-compose-ui-tooling").get())

            add("implementation", commonLibs.findLibrary("androidx.activity.compose").get())
            add("implementation", commonLibs.findLibrary("androidx.appcompat").get())
            add("implementation", commonLibs.findLibrary("androidx.compose.material3").get())
            add("implementation", commonLibs.findLibrary("androidx.compose.material.iconsExtended").get())
            add("implementation", commonLibs.findLibrary("androidx.compose.ui").get())
            add("implementation", commonLibs.findLibrary("androidx.compose.adaptive").get())
            add("implementation", commonLibs.findLibrary("androidx.lifecycle.runtime.compose").get())
            add("implementation", commonLibs.findLibrary("androidx.constraintlayout.compose").get())
        }

        testOptions {
            unitTests {
                // For Robolectric
                isIncludeAndroidResources = true
            }
        }
    }
}
