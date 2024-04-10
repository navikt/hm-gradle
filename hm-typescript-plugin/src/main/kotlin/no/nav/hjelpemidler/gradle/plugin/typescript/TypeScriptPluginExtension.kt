package no.nav.hjelpemidler.gradle.plugin.typescript

import org.gradle.api.Project

interface TypeScriptPluginExtension

fun Project.typeScript(configure: TypeScriptPluginExtension.() -> Unit) =
    extensions.configure(TypeScriptPluginExtension::class.java, configure)
