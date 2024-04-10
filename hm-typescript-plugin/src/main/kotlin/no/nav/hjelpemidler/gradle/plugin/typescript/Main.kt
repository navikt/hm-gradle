package no.nav.hjelpemidler.gradle.plugin.typescript

import no.nav.hjelpemidler.gradle.plugin.typescript.generator.TypeScriptGenerator
import java.io.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

fun main() {
    val generator = TypeScriptGenerator("no.nav.hjelpemidler.gradle.plugin.typescript.generator")

    generator.use {
        it.generate(User::class)
        println(it.toString())
    }

    Person::class.declaredMemberProperties.forEach {
        println(it.returnType)
    }

    println(Person::class.supertypes.map {
        it.toString() to (it.classifier as? KClass<*>)
    })
}

private data class Named<out T : Serializable>(
    val value: T,
    val description: String,
)

interface HasId {
    val id: String
}

private data class User(
    override val id: String,
    val codeA: Named<CodeA>,
    val codeB: Named<CodeB>,
    val codeC: Named<String>,
    val pair: Pair<String, Int>,
) : HasId

private data class Person(
    override val id: String,
    val aByB: Map<String, Int>,
) : HasId

private enum class CodeA { A, B, C }
private enum class CodeB { X, Y, Z }
