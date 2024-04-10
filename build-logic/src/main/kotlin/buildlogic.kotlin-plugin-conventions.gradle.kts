import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("buildlogic.kotlin-common-conventions")
    `java-gradle-plugin`
}

repositories {
    gradlePluginPortal()
}

// Gjør det mulig å bruke versjonskatalogen i convention plugins
// se https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
val libs = the<LibrariesForLibs>()

dependencies {
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useKotlinTest(libs.versions.kotlin)
        }

        val functionalTest by registering(JvmTestSuite::class) {
            useKotlinTest(libs.versions.kotlin)

            dependencies {
                implementation(project())
            }

            targets {
                all {
                    testTask.configure { shouldRunAfter(test) }
                }
            }
        }
    }
}

gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])

@Suppress("UnstableApiUsage")
tasks.named<Task>("check") {
    dependsOn(testing.suites.named("functionalTest"))
}
