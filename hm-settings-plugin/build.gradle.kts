plugins {
    id("buildlogic.kotlin-plugin-conventions")
}

@Suppress("UnstableApiUsage")
gradlePlugin {
    website = "https://github.com/navikt"
    vcsUrl = "https://github.com/navikt/hm-gradle"

    val settings by plugins.creating {
        id = "no.nav.hjelpemidler.settings"
        displayName = "NAV DigiHoT Gradle Settings Plugin"
        description = "Gradle settings plugin that configures common repositories and version catalog for our projects."
        tags = setOf("settings", "versionCatalog", "repositories")
        implementationClass = "no.nav.hjelpemidler.gradle.plugin.settings.SettingsPlugin"
    }
}
