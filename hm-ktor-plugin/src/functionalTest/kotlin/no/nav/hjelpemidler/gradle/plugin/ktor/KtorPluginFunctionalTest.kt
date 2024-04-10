package no.nav.hjelpemidler.gradle.plugin.ktor

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test

class KtorPluginFunctionalTest {
    @field:TempDir
    lateinit var projectDir: File

    private val buildFile by lazy { projectDir.resolve("build.gradle.kts") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle.kts") }

    @Test
    fun `Should apply the plugin`() {
        settingsFile.writeText("")
        buildFile.writeText(
            """
                plugins {
                    id("no.nav.hjelpemidler.ktor")
                }
            """.trimIndent()
        )

        val runner = GradleRunner.create()
            .forwardOutput()
            .withPluginClasspath()
            .withProjectDir(projectDir)

        assertDoesNotThrow {
            runner.build()
        }
    }
}
