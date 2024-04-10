package no.nav.hjelpemidler.gradle.plugin.typescript.generator

data class TypeScriptTypeParameter(
    val name: String,
    val constraint: TypeScriptType? = null,
    val default: TypeScriptType? = null,
) {
    override fun toString(): String = buildString {
        append(name)
        if (constraint != null) append(" extends $constraint")
        if (default != null) append(" = $default")
    }
}
