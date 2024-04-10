package no.nav.hjelpemidler.gradle.plugin.typescript.generator

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition

sealed class TypeScriptDefinition(val type: TypeScriptType) {
    abstract fun write(appendable: Appendable): Appendable

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TypeScriptDefinition
        return type == other.type
    }

    override fun hashCode(): Int = type.hashCode()

    override fun toString(): String = buildString { write(this) }

    companion object {
        const val INDENT = "    "
    }
}

class TypeScriptTypeDefinition(type: TypeScriptAlias) : TypeScriptDefinition(type) {
    private val aliasFor = type.aliasFor

    override fun write(appendable: Appendable): Appendable = appendable.append(
        """
            |/**
            | * @see ${type.java.typeScriptName}
            | */
            |export type $type = $aliasFor
            |
        """.trimMargin()
    )
}

class TypeScriptEnumDefinition(type: TypeScriptEnum) : TypeScriptDefinition(type) {
    private val formattedProperties: String by lazy {
        type.entries.joinToString(separator = "\n") { enumValue ->
            enumValue.format(INDENT)
        }
    }

    override fun write(appendable: Appendable): Appendable = appendable.append(
        """
            |/**
            | * @see ${type.java.typeScriptName}
            | */
            |export enum $type {
            |$formattedProperties
            |}
            |
        """.trimMargin()
    )
}

class TypeScriptInterfaceDefinition(
    type: TypeScriptInterface,
    val properties: List<BeanPropertyDefinition>,
) : TypeScriptDefinition(type) {
    private val formattedProperties: String by lazy {
        properties.joinToString(separator = "\n") { property ->
            property.format(INDENT)
        }
    }

    override fun write(appendable: Appendable): Appendable {
        return appendable.append(
            """
                |/**
                | * @see ${type.java.typeScriptName}
                | */
                |export interface $type {
                |$formattedProperties
                |}
                |
            """.trimMargin()
        )
    }
}

private fun Enum<*>.format(indent: String = TypeScriptDefinition.INDENT): String {
    return "$indent$name = '$name',"
}

private fun BeanPropertyDefinition.format(indent: String = TypeScriptDefinition.INDENT): String {
    val required = if (metadata.required == true) "" else "?"
    return "$indent$name$required: ${primaryType.typeScript}"
}
