package no.nav.hjelpemidler.gradle.plugin.typescript

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class TypeScriptPluginTest {
    @Test
    fun `Task generateTypeScriptDefinitions should exist`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("no.nav.hjelpemidler.typescript")

        val task = project.tasks.findByName("generateTypeScriptDefinitions")

        assertNotNull(task)
    }
}
