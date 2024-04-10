package no.nav.hjelpemidler.gradle.plugin.typescript.generator

import java.lang.reflect.Type

sealed class TypeScriptObject(
    java: Type,
    typeParameters: List<TypeScriptTypeParameter> = java.typeScriptTypeParameters,
) : TypeScriptType(java, typeParameters)

class TypeScriptArray(java: Type) : TypeScriptObject(java) {
    val elementType: TypeScriptType get() = unknownTypeScriptType // java.contentType.typeScript fixme

    override val name: String get() = "$elementType[]"
}

class TypeScriptRecord(java: Type) : TypeScriptObject(java) {
    val keyType: TypeScriptType get() = unknownTypeScriptType // java.keyType.typeScript fixme
    val valueType: TypeScriptType get() = unknownTypeScriptType // java.contentType.typeScript fixme

    override val name: String get() = "Record<$keyType, $valueType>"
}

class TypeScriptInterface(java: Type) : TypeScriptObject(java) {
    override val name: String
        get() {
            return java.typeScriptName // fixme
            /*
            val name = java.rawClass.simpleName
            return when {
                java.hasGenericTypes() -> buildString {
                    append(name)
                    java.bindings.typeParameters
                        .map(JavaType::typeScript)
                        .joinTo(this, separator = ", ", prefix = "<", postfix = ">")
                }

                else -> name
            }
            */
        }
}
