package no.nav.hjelpemidler.gradle.plugin.typescript.generator

import java.lang.reflect.Type

sealed class TypeScriptPrimitive(java: Type, override val name: String) : TypeScriptType(java)

class TypeScriptBoolean(java: Type) : TypeScriptPrimitive(java, "boolean")

class TypeScriptNumber(java: Type) : TypeScriptPrimitive(java, "number")

class TypeScriptString(java: Type) : TypeScriptPrimitive(java, "string")
