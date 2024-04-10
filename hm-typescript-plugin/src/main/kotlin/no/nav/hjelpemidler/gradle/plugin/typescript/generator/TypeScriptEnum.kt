package no.nav.hjelpemidler.gradle.plugin.typescript.generator

import com.fasterxml.jackson.databind.type.SimpleType

class TypeScriptEnum(java: SimpleType) : TypeScriptType(java) {
    init {
        require(java.isEnumType) { "Should be an enum type" }
    }

    val entries: List<Enum<*>> get() = emptyList() // java.rawClass.enumConstants?.filterIsInstance<Enum<*>>() ?: emptyList() fixme

    override val name: String = java.rawClass.simpleName
}
