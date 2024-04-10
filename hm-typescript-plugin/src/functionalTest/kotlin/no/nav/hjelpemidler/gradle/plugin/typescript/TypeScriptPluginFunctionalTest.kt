package no.nav.hjelpemidler.gradle.plugin.typescript

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test

class TypeScriptPluginFunctionalTest {
    @field:TempDir
    lateinit var projectDir: File

    private val buildFile by lazy { projectDir.resolve("build.gradle.kts") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle.kts") }

    @Test
    fun `Should execute task generateTypeScriptDefinitions`() {
        settingsFile.writeText(
            """
                rootProject.name = "hm-ktor-app"
            """.trimIndent()
        )
        buildFile.writeText(
            """
                import no.nav.hjelpemidler.gradle.plugin.typescript.GenerateTypeScriptDefinitionsTask
                
                plugins {
                    `embedded-kotlin`
                    id("no.nav.hjelpemidler.typescript")
                }
                
                tasks.withType<GenerateTypeScriptDefinitionsTask> {
                    acceptPackages.add("no.nav.hjelpemidler.gradle.plugin.typescript")
                }
            """.trimIndent()
        )

        val runner = GradleRunner.create()
            .forwardOutput()
            .withPluginClasspath()
            .withArguments("generateTypeScriptDefinitions")
            .withProjectDir(projectDir)
        val result = runner.build()
    }
}
