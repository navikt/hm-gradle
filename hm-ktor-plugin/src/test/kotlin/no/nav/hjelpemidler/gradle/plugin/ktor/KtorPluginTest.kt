package no.nav.hjelpemidler.gradle.plugin.ktor

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test

class KtorPluginTest {
    @Test
    fun `Should apply the plugin`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("no.nav.hjelpemidler.ktor")
    }
}
