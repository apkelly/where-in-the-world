import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.impl.VariantOutputImpl
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationFilenameConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<ApplicationAndroidComponentsExtension> {
                onVariants { variant ->
                    variant.outputs
                        .filterIsInstance<VariantOutputImpl>()
                        .forEach { output ->
                            // Note: This code doesn't change the names of "bundles", just APK builds :-(
                            val variantVersionName = variant.outputs.first().versionName.get()
                            val variantVersionCode = variant.outputs.first().versionCode.get()

                            val builtType = variantVersionName.split(" ").last()
                            val versionName = variantVersionName.split(" ").first()
                            val versionCode = if (variantVersionCode != 1) "$variantVersionCode" else "dev"

                            output.outputFileName.set(
                                "where-in-the-world-${builtType}-${versionName} ($versionCode).apk"
                            )
                        }
                }
            }
        }
    }
}
