package no.nav.hjelpemidler.gradle.plugin.settings

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test

class SettingsPluginFunctionalTest {
    @field:TempDir
    lateinit var projectDir: File

    private val buildFile by lazy { projectDir.resolve("build.gradle.kts") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle.kts") }

    @Test
    fun `Settings plugin is applied`() {
        settingsFile.writeText(
            """
                import no.nav.hjelpemidler.gradle.plugin.settings.SettingsPluginExtension
                
                plugins {
                    id("no.nav.hjelpemidler.settings")
                }
                
                configure<SettingsPluginExtension> {
                    catalogVersion = "0.1.36"
                }
            """.trimIndent()
        )
        buildFile.writeText(
            """
                plugins {
                }
            """.trimIndent()
        )

        val runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(projectDir)

        assertDoesNotThrow {
            runner.build()
        }
    }
}
