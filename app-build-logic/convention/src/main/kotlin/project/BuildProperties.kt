package project

import org.gradle.api.Project
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Properties

class BuildProperties(
    rootProject: Project
) {
    private val localProperties = Properties()

    init {
        try {
            // Load the keystore properties from a file in the project directory.
            val localPropertiesFile = rootProject.file("../local.properties")
            localProperties.load(FileInputStream(localPropertiesFile))
        } catch (_: FileNotFoundException) {
            println("Warning: local.properties file not found. Using environment variables instead.")
        }
    }

    // Read a property from local.properties or a GitHub Secret.
    fun readProperty(name: String): String? {
        val property = System.getenv(name) ?: localProperties[name] as? String
        if (property == null) {
            println("Missing environment variable or local.properties entry for \"$name\"")
        }
        return property
    }
}

