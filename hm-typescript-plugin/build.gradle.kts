plugins {
    id("buildlogic.kotlin-plugin-conventions")
}

dependencies {
    implementation(libs.classgraph)

    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)

    implementation(libs.microprofile.openapi.api)

    testImplementation(libs.microprofile.openapi.api)
    testImplementation(libs.ktor.server.core)
}

gradlePlugin {
    val typescript by plugins.creating {
        id = "no.nav.hjelpemidler.typescript"
        implementationClass = "no.nav.hjelpemidler.gradle.plugin.typescript.TypeScriptPlugin"
    }
}
