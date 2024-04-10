package no.nav.hjelpemidler.gradle.plugin.typescript.introspection

import io.github.classgraph.ScanResult
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBodySchema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema
import kotlin.reflect.KClass

internal fun interface Introspector {
    fun introspect(result: ScanResult): Sequence<KClass<*>>
}

internal val requestBodySchemaIntrospector = Introspector { result ->
    result.findMethodAnnotations<RequestBodySchema>().map(RequestBodySchema::value)
}

internal val apiResponseSchemaIntrospector = Introspector { result ->
    result.findMethodAnnotations<APIResponseSchema>().map(APIResponseSchema::value)
}
