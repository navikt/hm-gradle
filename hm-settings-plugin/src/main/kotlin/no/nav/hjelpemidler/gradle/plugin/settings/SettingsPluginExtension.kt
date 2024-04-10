package no.nav.hjelpemidler.gradle.plugin.settings

import org.gradle.api.provider.Property

interface SettingsPluginExtension {
    val catalogVersion: Property<String>
}
