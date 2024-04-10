package no.nav.hjelpemidler.gradle.plugin.typescript

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.intellij.lang.annotations.Language

interface CreatePackageJsonParameters : WorkParameters {
    val packageJsonFile: RegularFileProperty
    val name: Property<String>
    val version: Property<String>
    val description: Property<String>
    val author: Property<String>
    val license: Property<String>
}

abstract class CreatePackageJsonAction : WorkAction<CreatePackageJsonParameters> {
    override fun execute() {
        val name = parameters.name.get()
        val version = parameters.version.get()
        val description = parameters.description.get()
        val author = parameters.author.getOrElse("Team DigiHoT")
        val license = parameters.license.getOrElse("UNLICENSED")

        @Language("JSON")
        val packageJson = """
            |{
            |  "name": "$name",
            |  "version": "$version",
            |  "description": "$description",
            |  "author": "$author",
            |  "license": "$license",
            |  "main": "",
            |  "types": "dist/index.d.ts",
            |  "scripts": {
            |    "build": "tsc -p ."
            |  },
            |  "devDependencies": {
            |    "@types/web": "*",
            |    "typescript": "*"
            |  }
            |}
            |
        """.trimMargin()

        val packageJsonFile = parameters.packageJsonFile.asFile.get()
        packageJsonFile.createParentDirectoryIfNotExists()
        packageJsonFile.writeText(packageJson)
    }
}
