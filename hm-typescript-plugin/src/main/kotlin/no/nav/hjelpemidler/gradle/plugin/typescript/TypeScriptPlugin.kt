package no.nav.hjelpemidler.gradle.plugin.typescript

import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class TypeScriptPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val configuration = project.configurations.create("typeScriptPluginConfiguration") {
        }

        val extension = project.extensions.create(
            "typeScriptPluginExtension",
            TypeScriptPluginExtension::class.java,
        )

        val task = project.tasks.register(
            "generateTypeScriptDefinitions",
            GenerateTypeScriptDefinitionsTask::class.java,
        ) {
        }
    }
}
