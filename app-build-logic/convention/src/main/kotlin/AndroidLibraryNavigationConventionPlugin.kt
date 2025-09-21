import project.commonLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryNavigationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                val bom = commonLibs.findLibrary("androidx-compose-bom").get()
                add("implementation", platform(bom))
                add("api", commonLibs.findLibrary("androidx.navigation3.runtime").get())
                add("implementation", commonLibs.findLibrary("androidx.navigation3.ui").get())
            }
        }
    }
}
