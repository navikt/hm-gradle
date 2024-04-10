package no.nav.hjelpemidler.gradle.plugin.typescript.generator

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.type.ArrayType
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.MapType
import com.fasterxml.jackson.databind.type.SimpleType
import java.lang.reflect.Type
import java.sql.Timestamp
import java.time.temporal.Temporal
import java.util.Calendar
import java.util.Date
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

val Type.typeScript: TypeScriptType
    get() = when (this) {
        is ArrayType, is CollectionType -> createType(this as JavaType, ::TypeScriptArray)

        is MapType -> createType(this, ::TypeScriptRecord)

        is SimpleType -> when {
            // Primitives
            isSubclassOf(Boolean::class) -> createType(this, ::TypeScriptBoolean)
            isSubclassOf(Number::class) -> createType(this, ::TypeScriptNumber)
            isSubclassOf(CharSequence::class) -> createType(this, ::TypeScriptString)

            // Enum
            isSubclassOf(Enum::class) -> createType(this, ::TypeScriptEnum)

            // Aliases
            isSubclassOf(Calendar::class) -> createAlias(this, ::TypeScriptString)
            isSubclassOf(Date::class) -> createAlias(this, ::TypeScriptString)
            isSubclassOf(Temporal::class) -> createAlias(this, ::TypeScriptString)
            isSubclassOf(Timestamp::class) -> createAlias(this, ::TypeScriptString)
            isSubclassOf(UUID::class) -> createAlias(this, ::TypeScriptString)

            // JsonNode
            isSubclassOf(ArrayNode::class) -> createAlias(this, ::TypeScriptAny) // fixme -> create TypeScriptArray
            isSubclassOf(ObjectNode::class) -> createAlias(this, ::TypeScriptAny) // fixme -> create TypeScriptRecord
            isSubclassOf(JsonNode::class) -> createAlias(this, ::TypeScriptAny)

            // Other
            isSubclassOf(Unit::class) -> createUnknown(this)
            isSubclassOf(Void::class) -> createUnknown(this)

            rawClass == Any::class.java -> createType(this, ::TypeScriptAny)

            this == unknownJavaType -> unknownTypeScriptType

            // Object
            else -> createType(this, ::TypeScriptInterface)
        }

        else -> createUnknown(this)
    }

val Type.typeScriptTypeParameters: List<TypeScriptTypeParameter>
    get() = if (this is JavaType) {
        (0..<containedTypeCount()).map { index ->
            TypeScriptTypeParameter(
                name = bindings.getBoundName(index) ?: "",
                constraint = bindings.getBoundType(index)?.typeScript,
                default = null
            )
        }
    } else {
        emptyList() // todo
    }

val Type.typeScriptName: String get() = ""

private fun JavaType.isSubclassOf(kClass: KClass<*>): Boolean =
    rawClass.kotlin.isSubclassOf(kClass)

private val typeScriptTypeByJavaType: MutableMap<Type, TypeScriptType> =
    ConcurrentHashMap<Type, TypeScriptType>()

private fun <K : Type> createType(key: K, block: (K) -> TypeScriptType): TypeScriptType =
    typeScriptTypeByJavaType.computeIfAbsent(key) { block(key) }

private fun <K : Type> createAlias(key: K, block: (K) -> TypeScriptType): TypeScriptType =
    createType(key) { block(it).aliased() }

private fun <K : Type> createUnknown(key: K): TypeScriptType =
    createType(key, ::TypeScriptUnknown)
