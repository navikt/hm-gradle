package no.nav.hjelpemidler.gradle.plugin.typescript

import org.gradle.api.file.RegularFileProperty
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.intellij.lang.annotations.Language

interface CreateTSConfigJsonParameters : WorkParameters {
    val tsConfigJsonFile: RegularFileProperty
}

abstract class CreateTSConfigJsonAction : WorkAction<CreateTSConfigJsonParameters> {
    override fun execute() {
        val tsConfigJsonFile = parameters.tsConfigJsonFile.asFile.get()
        tsConfigJsonFile.createParentDirectoryIfNotExists()
        tsConfigJsonFile.createNewFile()
        tsConfigJsonFile.writeText(tsConfig)
    }
}

@Language("JSON")
private val tsConfig = """
    |{
    |  "compilerOptions": {
    |    "declaration": true,
    |    "lib": ["DOM", "DOM.Iterable", "ESNext"],
    |    "module": "ESNext",
    |    "moduleResolution": "Node",
    |    "outDir": "./dist",
    |    "skipLibCheck": true,
    |    "strict": true,
    |    "target": "ESNext"
    |  },
    |  "include": ["src"]
    |}
    |
""".trimMargin()
