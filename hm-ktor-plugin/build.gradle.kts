plugins {
    id("buildlogic.kotlin-plugin-conventions")
}

dependencies {
    implementation(libs.ktor.gradle.plugin)
    implementation(libs.spotless.gradle.plugin)
}

gradlePlugin {
    val ktor by plugins.creating {
        id = "no.nav.hjelpemidler.ktor"
        implementationClass = "no.nav.hjelpemidler.gradle.plugin.ktor.KtorPlugin"
    }
}
