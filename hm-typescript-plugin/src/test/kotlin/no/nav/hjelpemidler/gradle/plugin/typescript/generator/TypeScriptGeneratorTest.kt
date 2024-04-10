package no.nav.hjelpemidler.gradle.plugin.typescript.generator

import kotlin.test.Test

class TypeScriptGeneratorTest {
    private val generator = TypeScriptGenerator("no.nav.hjelpemidler.gradle.plugin.typescript.generator")

    @Test
    fun `Should generate typescript definitions`() {
        generator.use {
        }
    }
}
