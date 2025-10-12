import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationVersionConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val computedVersionCode = 22
            val computedVersionName = "2.0.0"

            println("computeVersionCode : $computedVersionCode")
            println("computeVersionName : $computedVersionName")

            configure<BaseAppModuleExtension> {
                defaultConfig {
                    versionCode = computedVersionCode
                    versionName = computedVersionName
                }
            }

            subprojects.forEach {
                it.configure<BaseAppModuleExtension> {
                    defaultConfig {
                        buildConfigField("String", "VERSION_NAME", "\"$computedVersionName\"")
                        buildConfigField("int", "VERSION_CODE", "$computedVersionCode}")
                    }
                }
            }
        }
    }
}
