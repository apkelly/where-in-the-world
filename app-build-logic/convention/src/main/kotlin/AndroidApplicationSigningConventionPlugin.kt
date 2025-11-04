

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import project.BuildProperties

class AndroidApplicationSigningConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val buildProperties = BuildProperties(target)

            configure<BaseAppModuleExtension> {
                signingConfigs {
//                    getByName("debug") {
//                      storeFile = file("keystore/google_play_upload.keystore")
//                      storePassword = readProperty("storePassword")
//                      keyAlias = readProperty("debugAlias")
//                      keyPassword = readProperty("debugPassword")
//                    }
                    create("release") {
                        storeFile = file(buildProperties.readProperty("storeFile"))
                        storePassword = buildProperties.readProperty("storePassword")
                        keyAlias = buildProperties.readProperty("keyAlias")
                        keyPassword = buildProperties.readProperty("keyPassword")
                    }
                }

                buildTypes {
                    debug {
                        isMinifyEnabled = false
//                        signingConfig = signingConfigs.getByName("debug")
                        applicationIdSuffix = ".dev"
                    }

                    release {
                        isMinifyEnabled = false
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
