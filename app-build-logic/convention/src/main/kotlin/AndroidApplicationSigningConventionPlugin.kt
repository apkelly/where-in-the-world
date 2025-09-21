

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.util.Properties
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class AndroidApplicationSigningConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val keystoreProperties = Properties()
            try {
                // Load the keystore properties from a file in the project directory.
                val keystorePropertiesFile: File = file("keystore/keystore.properties")
                keystoreProperties.load(FileInputStream(keystorePropertiesFile))
            } catch (_: FileNotFoundException) {
                println("Warning: keystore.properties file not found. Using environment variables instead.")
            }

            // Read a property from ~/.gradle/gradle.properties or a GitHub Secret.
            fun readProperty(name: String): String {
//                return System.getenv(envName) ?: project.findProperty(name) as? String
                return System.getenv(name.uppercase()) ?: keystoreProperties[name] as? String
                ?: throw RuntimeException("Missing environment variable or gradle property for \"$name\"")
            }

            configure<BaseAppModuleExtension> {
                signingConfigs {
//                    getByName("debug") {
//                      storeFile = file("keystore/google_play_upload.keystore")
//                      storePassword = readProperty("storePassword")
//                      keyAlias = readProperty("debugAlias")
//                      keyPassword = readProperty("debugPassword")
//                    }
                    create("release") {
                        storeFile = file("keystore/google_play_upload.keystore")
                        storePassword = readProperty("storePassword")
                        keyAlias = readProperty("keyAlias")
                        keyPassword = readProperty("keyPassword")
                    }
                }

                buildTypes {
                    debug {
                        isMinifyEnabled = false
//                        signingConfig = signingConfigs.getByName("debug")
                        applicationIdSuffix = ".dev"
                    }

                    release {
                        isMinifyEnabled = true
                        signingConfig = signingConfigs.getByName("release")
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
            }
        }
    }
}
