package no.nav.hjelpemidler.gradle.plugin.typescript.introspection

import io.github.classgraph.ScanResult

internal inline fun <reified T : Annotation> ScanResult.findMethodAnnotations(): Sequence<T> {
    val clazz = T::class.java
    return getClassesWithMethodAnnotation(clazz)
        .asSequence()
        .flatMap { it.getMethodInfo("invokeSuspend") }
        .map { it.loadClassAndGetMethod() }
        .mapNotNull { it.getAnnotation(clazz) }
}
