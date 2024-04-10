package no.nav.hjelpemidler.gradle.plugin.typescript

import java.io.File

fun File.createParentDirectoryIfNotExists() {
    val it = parentFile
    require(it.isDirectory || it.mkdirs()) { "${it.absolutePath} could not be created" }
}
