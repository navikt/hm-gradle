package no.nav.hjelpemidler.gradle.plugin.typescript

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkerExecutor
import javax.inject.Inject

abstract class GenerateTypeScriptDefinitionsTask : DefaultTask() {
    @get:Classpath
    @get:InputFiles
    val pluginClasspath: ConfigurableFileCollection = project.objects.fileCollection()

    @get:Input
    val acceptPackages: SetProperty<String> = project.objects.setProperty(String::class.java)

    @get:Input
    val version: Property<String> = project.objects.property(String::class.java)

    @get:OutputDirectory
    val outputDirectory: DirectoryProperty = project.objects.directoryProperty()

    @get:Inject
    abstract val workerExecutor: WorkerExecutor

    init {
        version.convention("0.0.1")
        outputDirectory.convention(project.layout.buildDirectory.dir("generated/typescript/@types/${project.rootProject.name}"))
    }

    @TaskAction
    fun action() {
        val sourceSetContainer = project.extensions.getByType(SourceSetContainer::class.java)
        val runtimeClasspath = sourceSetContainer.getByName("main").runtimeClasspath

        val workQueue = workerExecutor.classLoaderIsolation {
            it.classpath.from(runtimeClasspath)
        }

        workQueue.submit(CreatePackageJsonAction::class.java) { parameters ->
            parameters.packageJsonFile.set(outputDirectory.file("package.json"))
            parameters.name.set("@types/${project.rootProject.name}")
            parameters.version.set(version)
            parameters.description.set(project.rootProject.name)
            // parameters.author.set("")
            // parameters.license.set("")
        }

        workQueue.submit(CreateTSConfigJsonAction::class.java) { parameters ->
            parameters.tsConfigJsonFile.set(outputDirectory.file("tsconfig.json"))
        }

        workQueue.submit(IntrospectionTask::class.java) { parameters ->
            parameters.acceptPackages.set(acceptPackages)
            parameters.outputDirectory.set(outputDirectory)
            parameters.indexFile.set(outputDirectory.file("src/index.ts"))
        }

        workQueue.await()
    }
}
