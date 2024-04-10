package no.nav.hjelpemidler.gradle.plugin.typescript.introspection

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult

internal fun scanPackages(vararg acceptPackages: String): ScanResult =
    ClassGraph()
        .enableAllInfo()
        .acceptPackages(*acceptPackages)
        .scan()

internal fun scanPackages(acceptPackages: Set<String>): ScanResult =
    scanPackages(*acceptPackages.toTypedArray())
