package no.nav.hjelpemidler.gradle.plugin.typescript.generator

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.github.classgraph.ClassInfo
import io.github.classgraph.ScanResult
import no.nav.hjelpemidler.gradle.plugin.typescript.introspection.Introspector
import no.nav.hjelpemidler.gradle.plugin.typescript.introspection.defaultObjectMapper
import no.nav.hjelpemidler.gradle.plugin.typescript.introspection.scanPackages
import java.io.Closeable
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

class TypeScriptGenerator(
    objectMapper: ObjectMapper = defaultObjectMapper,
    acceptPackages: Set<String>,
) : Closeable {
    constructor(vararg acceptPackages: String) : this(acceptPackages = acceptPackages.toSet())

    private val scanResult = scanPackages(acceptPackages)
    private val context = TSGeneratorContext(objectMapper, scanResult, mutableMapOf())

    fun generate(kClass: KClass<*>) {
        context.process(kClass)
    }

    fun generate(typeReference: TypeReference<*>) {
        context.process(typeReference)
    }

    internal fun generate(introspector: Introspector) {
        introspector.introspect(scanResult).forEach(::generate)
    }

    inline fun <reified T> generate() {
        generate(jacksonTypeRef<T>())
    }

    fun write(appendable: Appendable) {
        context.write<TypeScriptTypeDefinition>(appendable)
        context.write<TypeScriptEnumDefinition>(appendable)
        context.writeInterface(appendable)
    }

    override fun toString(): String = buildString { write(this) }

    override fun close() {
        scanResult.close()
    }
}

class TSGeneratorContext(
    private val objectMapper: ObjectMapper,
    private val scanResult: ScanResult,
    private val definitions: MutableMap<TypeScriptType, TypeScriptDefinition>,
) : Map<TypeScriptType, TypeScriptDefinition> by definitions {
    private fun constructType(kClass: KClass<*>): JavaType =
        objectMapper.constructType(kClass.java)

    private fun constructType(typeReference: TypeReference<*>): JavaType =
        objectMapper.constructType(typeReference)

    private fun introspect(type: JavaType): BeanDescription =
        objectMapper.serializationConfig.introspect(type)

    private fun introspect(type: TypeScriptType): BeanDescription =
        introspect(type.java as JavaType)

    private fun getClassInfo(kClass: KClass<*>): ClassInfo? =
        scanResult.getClassInfo(kClass.jvmName)

    private fun getClassInfo(type: TypeScriptType): ClassInfo? =
        null // getClassInfo(type.java.rawClass.kotlin) fixme

    private fun <T : TypeScriptType> define(type: T, block: (T) -> TypeScriptDefinition): TypeScriptDefinition =
        definitions.computeIfAbsent(type) { block(type) }

    private fun process(type: TypeScriptType) {
        when (type) {
            is TypeScriptAlias -> define(type, ::TypeScriptTypeDefinition)
            is TypeScriptEnum -> define(type, ::TypeScriptEnumDefinition)

            is TypeScriptArray -> process(type.elementType)

            is TypeScriptInterface -> {
                val properties = introspect(type).findProperties()

                // Prevents processing e.g. java.io.Serializable
                if (properties.isEmpty()) return

                define(type) { TypeScriptInterfaceDefinition(it, properties) }

                // Process all properties, interfaces and type parameters
                properties.forEach { process(it.primaryType.typeScript) }
                // type.java.interfaces.forEach { process(it.typeScript) } fixme
                // type.java.bindings.typeParameters.forEach { process(it.typeScript) } fixme
            }

            is TypeScriptRecord -> {
                process(type.keyType)
                process(type.valueType)
            }

            else -> return
        }
    }

    fun process(kClass: KClass<*>) {
        val type = constructType(kClass)
        process(type.typeScript)
    }

    fun process(typeReference: TypeReference<*>) {
        val type = constructType(typeReference)
        process(type.typeScript)
    }

    inline fun <reified T : TypeScriptDefinition> write(appendable: Appendable) {
        values.filterIsInstance<T>().forEach { it.write(appendable).appendLine() }
    }

    fun writeInterface(appendable: Appendable) {
        values
            .filterIsInstance<TypeScriptInterfaceDefinition>()
            .groupBy { it.type.java } // fixme
            .flatMap { (type, definitions) ->
                // Named<T = CodeA | CodeB | string>
                definitions
            }
            .forEach { it.write(appendable).appendLine() }
    }
}
