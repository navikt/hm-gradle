plugins {
    id("buildlogic.kotlin-plugin-conventions")
}

gradlePlugin {
    val settings by plugins.creating {
        id = "no.nav.hjelpemidler.settings"
        implementationClass = "no.nav.hjelpemidler.gradle.plugin.settings.SettingsPlugin"
    }
}
