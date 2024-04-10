package no.nav.hjelpemidler.gradle.plugin.typescript.generator

data class TypeScriptProperty(val name: String, val type: TypeScriptType, val required: Boolean) {
    override fun toString(): String {
        val indent = TypeScriptDefinition.INDENT
        val required = if (required) "" else "?"
        return "$indent$name$required: $type"
    }
}
