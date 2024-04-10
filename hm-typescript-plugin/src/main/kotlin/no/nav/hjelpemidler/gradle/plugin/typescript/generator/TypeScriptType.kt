package no.nav.hjelpemidler.gradle.plugin.typescript.generator

import java.lang.reflect.Type

sealed class TypeScriptType(
    val java: Type,
    val typeParameters: List<TypeScriptTypeParameter> = java.typeScriptTypeParameters,
) : Type by java {
    abstract val name: String

    fun aliased(): TypeScriptAlias = TypeScriptAlias(this)

    override fun toString(): String = name
}

class TypeScriptAlias(val aliasFor: TypeScriptType) : TypeScriptType(aliasFor.java) {
    override val name: String = typeScriptName
}

class TypeScriptAny(java: Type) : TypeScriptType(java) {
    override val name: String = "any"
}

class TypeScriptUnknown(java: Type = unknownJavaType) : TypeScriptType(java) {
    override val name: String = "unknown"
}
