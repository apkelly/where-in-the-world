package project

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.TestReport
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        compileSdk = commonLibs.findVersion("compileSdk").get().toString().toInt()
        defaultConfig.apply {
            minSdk = commonLibs.findVersion("minSdk").get().toString().toInt()
        }

        compileOptions.apply {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
            isCoreLibraryDesugaringEnabled = true
        }

        buildFeatures.buildConfig = true
    }

    extensions.configure<KotlinProjectExtension> {
        jvmToolchain(JvmTarget.JVM_21.target.toInt())
    }

    dependencies {
        add("coreLibraryDesugaring", commonLibs.findLibrary("core-library-desugaring").get())
        add("testImplementation", commonLibs.findLibrary("junit4").get())
        add("testImplementation", commonLibs.findLibrary("mockk").get())
        add("testImplementation", commonLibs.findLibrary("kotlinx-coroutines-test").get())
    }

    val mergedTestReport = rootProject.tasks.maybeCreate("mergedTestReport", TestReport::class.java).apply {
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("reports/tests/testDebugUnitTest"))
    }

    afterEvaluate {
        tasks.matching { it.name == "testDebugUnitTest" }.configureEach {
            finalizedBy(mergedTestReport)
            mergedTestReport.testResults.from(
                layout.buildDirectory.dir("test-results/testDebugUnitTest/binary")
            )
        }
    }
}
