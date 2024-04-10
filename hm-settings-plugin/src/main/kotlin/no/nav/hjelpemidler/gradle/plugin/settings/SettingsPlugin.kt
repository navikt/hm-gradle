package no.nav.hjelpemidler.gradle.plugin.settings

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import java.net.URI

abstract class SettingsPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        val extension = settings.extensions.create(
            "hjelpemidlerSettings",
            SettingsPluginExtension::class.java,
        )

        settings.dependencyResolutionManagement.apply {
            @Suppress("UnstableApiUsage")
            repositories.apply {
                mavenCentral()

                maven {
                    it.url = URI.create("https://maven.pkg.github.com/navikt/*")
                    it.credentials.apply {
                        username = System.getenv("GITHUB_ACTOR")
                        password = System.getenv("GITHUB_TOKEN")
                    }
                }

                maven {
                    it.url = URI.create("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
                }
            }

            versionCatalogs.apply {
                create("libs").apply {
                    from("no.nav.hjelpemidler:hm-katalog:0.1.37")
                }
            }
        }
    }
}
