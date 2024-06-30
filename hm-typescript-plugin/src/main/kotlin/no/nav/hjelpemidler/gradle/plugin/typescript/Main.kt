package no.nav.hjelpemidler.gradle.plugin.typescript

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import java.io.Serializable
import java.time.Instant
import java.time.temporal.Temporal
import java.util.Calendar
import java.util.Date
import java.util.UUID
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.typeOf

fun main() {
    User::class.typeScriptProperties.forEach {
        // println(it)
    }

    println()

    Named::class.typeScriptProperties.forEach {
        // println(it)
    }
}

class TSProperty(val parent: KClass<*>, val name: String, val type: TSType, val optional: Boolean) {
    override fun toString(): String = if (optional) "$name?: $type" else "$name: $type"
}

val <T : Any> KClass<T>.typeScriptProperties: List<TSProperty>
    get() {
        val fromParameters = primaryConstructor?.parameters?.map {
            TSProperty(this, it.typeScriptName, it.type.typeScript, it.type.isMarkedNullable)
        }

        val fromProperties = declaredMemberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .map {
                val returnType = it.returnType
                TSProperty(this, it.typeScriptName, returnType.typeScript, returnType.isMarkedNullable)
            }
            .forEach {
                println(it)
            }

        return fromParameters ?: emptyList()
    }

val KParameter.typeScriptName: String
    get() {
        val jsonPropertyValue = jsonProperty?.value
        if (!jsonPropertyValue.isNullOrEmpty()) return jsonPropertyValue
        return name ?: ""
    }

val KCallable<*>.typeScriptName: String
    get() {
        return name
    }

val KAnnotatedElement.jsonGetter: JsonGetter? get() = findAnnotation<JsonGetter>()
val KAnnotatedElement.jsonProperty: JsonProperty? get() = findAnnotation<JsonProperty>()
val KAnnotatedElement.jsonValue: JsonValue? get() = findAnnotation<JsonValue>()

sealed interface TSDefinition {
    fun write(appendable: Appendable)
}

sealed class TSType(val kotlin: KType, val name: String) {
    override fun toString() = name
}

object TSBoolean : TSType(typeOf<Boolean?>(), "boolean")
object TSNumber : TSType(typeOf<Number?>(), "number")
object TSString : TSType(typeOf<String?>(), "string")

object TSAny : TSType(typeOf<Any?>(), "any")
object TSUnknown : TSType(typeOf<Any?>(), "unknown")

class TSArray(kotlin: KType) : TSType(kotlin, "Array" + kotlin.foo)
class TSRecord(kotlin: KType) : TSType(kotlin, "Record" + kotlin.foo)

val KType.foo: String
    get() {
        return arguments.map { it.type?.typeScript ?: TSAny }.joinToString(", ", prefix = "<", postfix = ">")
    }

val KType.typeParameters: List<KTypeParameter>
    get() {
        return when (val classifier = this.classifier) {
            is KClass<*> -> classifier.typeParameters
            is KTypeParameter -> listOf(classifier)
            else -> emptyList()
        }
    }

class TSAlias(kotlin: KType, val aliasFor: TSType) : TSType(kotlin, kotlin.classifier.name), TSDefinition {
    override fun write(appendable: Appendable) {
        TODO("Not yet implemented")
    }
}

class TSEnum(kotlin: KType) : TSType(kotlin, kotlin.classifier.name), TSDefinition {
    override fun write(appendable: Appendable) {
        TODO("Not yet implemented")
    }
}

class TSObject(kotlin: KType) : TSType(kotlin, kotlin.classifier.name), TSDefinition {
    override fun write(appendable: Appendable) {
        TODO("Not yet implemented")
    }
}

val KType.typeScript: TSType
    get() {
        val result = when {
            // Primitives
            isSubtypeOf(TSBoolean.kotlin) -> TSBoolean
            isSubtypeOf(TSNumber.kotlin) -> TSNumber
            isSubtypeOf(TSString.kotlin) -> TSString

            // Enum
            isSubtypeOf(typeOf<Enum<*>>()) -> TSEnum(this)

            // Collections and Maps
            isSubtypeOf(typeOf<Collection<*>>()) -> TSArray(this)
            isSubtypeOf(typeOf<Map<*, *>>()) -> TSRecord(this)

            // Aliases
            isSubtypeOf(typeOf<Calendar?>()) -> TSAlias(this, TSString)
            isSubtypeOf(typeOf<Date?>()) -> TSAlias(this, TSString)
            isSubtypeOf(typeOf<Temporal?>()) -> TSAlias(this, TSString)
            isSubtypeOf(typeOf<UUID?>()) -> TSAlias(this, TSString)

            // Other
            else -> TSObject(this)
        }
        return result
    }

val KClassifier?.name: String
    get() = when (this) {
        is KClass<*> -> simpleName ?: ""
        is KTypeParameter -> name
        else -> ""
    }

private data class Named<out T : Serializable>(
    val value: T,
    val description: String,
)

interface HasId {
    val id: String
}

private data class User(
    @get:JsonProperty("userId")
    override val id: String,
    val codeA: Named<CodeA>,
    val codeB: CodeB,
    val codeC: Named<String>?,
    @JsonProperty("foobar")
    val pair: Pair<String, Int>,
    val otherId: Id,
    val yetAnotherId: Long,
    val createdAt: Instant,
    val flag: Boolean,
    val otherFlag: Boolean?,
    @get:JsonGetter
    val personId: UUID,
    val items: List<String>,
    val categories: Map<String, Long>,
) : HasId {
    @JsonProperty("user")
    val name: String = "test"

    @get:JsonProperty("userAge")
    val age: Int = -1

    @get:JsonGetter("town")
    val city: String = ""
}

data class Id @JsonCreator constructor(@JsonValue val value: String)

private enum class CodeA { A, B, C }
private enum class CodeB { X, Y, Z }
