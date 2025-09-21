import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationFilenameConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configure<BaseAppModuleExtension> {
                applicationVariants.all {
                    val variant = this
                    variant.outputs
                        .map { it as BaseVariantOutputImpl }
                        .forEach { output ->
                            // Note: This code doesn't change the names of "bundles", just APK builds :-(

                            val versionCode =
                                if (variant.versionCode != 1) "${variant.versionCode}" else "dev"
                            output.outputFileName =
                                "where-in-the-world-${output.baseName}-${variant.versionName} ($versionCode).apk"
                        }
                }
            }
        }
    }
}
