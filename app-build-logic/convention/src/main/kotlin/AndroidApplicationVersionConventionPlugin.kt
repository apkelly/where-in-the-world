import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationVersionConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val computedVersionCode = 24
            val computedVersionName = "2.0.2"

            println("computeVersionCode : $computedVersionCode")
            println("computeVersionName : $computedVersionName")

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    versionCode = computedVersionCode
                    versionName = computedVersionName
                }
            }

            subprojects.forEach {
                extensions.configure<ApplicationExtension> {
                    defaultConfig {
                        buildConfigField("String", "VERSION_NAME", "\"$computedVersionName\"")
                        buildConfigField("int", "VERSION_CODE", "$computedVersionCode}")
                    }
                }
            }
        }
    }
}
