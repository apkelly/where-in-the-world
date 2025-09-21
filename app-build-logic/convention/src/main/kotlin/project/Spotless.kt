package project

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureSpotless(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    with(pluginManager) {
        apply(commonLibs.findPlugin("spotless").get().get().pluginId)
    }

    commonExtension.apply {
        lint {
            abortOnError = false
        }
    }

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        format("misc") {
            // define the files to apply `misc` to
            target("**/*.gradle", "**/*.md", "**/.gitignore")

            // define the steps to apply to those files
            trimTrailingWhitespace()
            leadingTabsToSpaces()
            endWithNewline()
            isEnforceCheck = false
        }

        kotlin {
            target("**/*.kt")

            ktlint()
                .editorConfigOverride(
                    mapOf(
                        "ktlint_code_style" to "android_studio",
                        "ktlint_standard_no-wildcard-imports" to "disabled",
                        "ktlint_standard_function-naming" to "disabled",
                        "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
                        "ij_kotlin_allow_trailing_comma" to "true",
                        "indent_size" to "4",
                        "continuation_indent_size" to "4",
                        "disabled_rules" to "no-wildcard-imports",
                        "max_line_length" to "off",
                        "ktlint_function_signature_rule_force_multiline_when_parameter_count_greater_or_equal_than" to 1
                    ),
                )

            trimTrailingWhitespace()
            leadingTabsToSpaces()
            endWithNewline()
            isEnforceCheck = false
        }
    }
}
