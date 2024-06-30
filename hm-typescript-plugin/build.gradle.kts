plugins {
    id("buildlogic.kotlin-plugin-conventions")
}

dependencies {
    implementation(libs.classgraph)
    implementation(libs.jackson.annotations)
    implementation(libs.microprofile.openapi.api)

    runtimeOnly(libs.kotlin.reflect)

    testImplementation(libs.ktor.server.core)
}

gradlePlugin {
    val typescript by plugins.creating {
        id = "no.nav.hjelpemidler.typescript"
        implementationClass = "no.nav.hjelpemidler.gradle.plugin.typescript.TypeScriptPlugin"
    }
}
