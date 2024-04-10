package no.nav.hjelpemidler.gradle.plugin.typescript.introspection

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBodySchema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema
import kotlin.test.Test

class IntrospectorTest {
    @Test
    fun `Should find method annotations`() {
        scanPackages("no.nav.hjelpemidler.gradle.plugin.typescript.introspection").use { result ->
            result.findMethodAnnotations<RequestBodySchema>().forEach {
                println(it.value)
            }
            result.findMethodAnnotations<APIResponseSchema>().forEach {
                println(it.value)
            }
        }
    }
}

@Suppress("unused")
fun Route.api() {
    post @RequestBodySchema(Request::class) { call.respond(HttpStatusCode.NoContent) }
    get @APIResponseSchema(Response::class) { call.respond("TEST") }
}

data class Request(
    val name: String,
    val age: Int?,
)

data class Response(
    val id: String,
    val name: String,
    val age: Int?,
)
