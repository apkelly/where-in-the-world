import com.android.build.api.dsl.ApplicationExtension
import project.commonLibs
import project.configureKotlinAndroid
import project.configureDependencies
import project.configureSpotless
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                configureDependencies(this)
                configureSpotless(this)
                defaultConfig.targetSdk = commonLibs.findVersion("targetSdk").get().toString().toInt()
                testOptions.animationsDisabled = true
            }
        }
    }

}
