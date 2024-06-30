package no.nav.hjelpemidler.gradle.plugin.settings

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import java.net.URI

abstract class SettingsPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        val catalogVersionProvider = settings
            .providers
            .gradleProperty("hmKatalogVersion")
            .orElse("0.2.24")

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
                    val version = catalogVersionProvider.get()
                    println("hmKatalogVersion: $version")
                    from("no.nav.hjelpemidler:hm-katalog:$version")
                }
            }
        }
    }
}
