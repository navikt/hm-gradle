pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "hm-gradle"
include("hm-ktor-plugin", "hm-settings-plugin", "hm-typescript-plugin")
