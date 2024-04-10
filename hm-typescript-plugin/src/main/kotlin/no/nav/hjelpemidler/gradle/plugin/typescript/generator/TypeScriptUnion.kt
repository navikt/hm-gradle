package no.nav.hjelpemidler.gradle.plugin.typescript.generator

class TypeScriptUnion(vararg val type: TypeScriptType) : TypeScriptType(unknownJavaType) {
    override val name: String get() = type.joinToString(" | ")
}
