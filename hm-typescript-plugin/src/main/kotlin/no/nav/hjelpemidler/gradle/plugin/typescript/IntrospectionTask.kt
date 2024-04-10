package no.nav.hjelpemidler.gradle.plugin.typescript

import no.nav.hjelpemidler.gradle.plugin.typescript.generator.TypeScriptGenerator
import no.nav.hjelpemidler.gradle.plugin.typescript.introspection.apiResponseSchemaIntrospector
import no.nav.hjelpemidler.gradle.plugin.typescript.introspection.requestBodySchemaIntrospector
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.SetProperty
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters

interface IntrospectionTaskParameters : WorkParameters {
    val acceptPackages: SetProperty<String>
    val outputDirectory: DirectoryProperty
    val indexFile: RegularFileProperty
}

abstract class IntrospectionTask : WorkAction<IntrospectionTaskParameters> {
    override fun execute() {
        val acceptPackages = parameters.acceptPackages.get()
        TypeScriptGenerator(acceptPackages = acceptPackages).use { generator ->
            generator.generate(requestBodySchemaIntrospector)
            generator.generate(apiResponseSchemaIntrospector)

            println("Finished!")
            println(generator.toString())
        }
    }
}
