package no.nav.hjelpemidler.gradle.plugin.typescript.introspection

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder

internal val defaultObjectMapper: ObjectMapper by lazy {
    jacksonMapperBuilder()
        .addModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build()
}
