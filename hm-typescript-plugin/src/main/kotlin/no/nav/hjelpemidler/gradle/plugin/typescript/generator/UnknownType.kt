package no.nav.hjelpemidler.gradle.plugin.typescript.generator

import com.fasterxml.jackson.databind.type.TypeFactory
import java.lang.reflect.Type

internal val unknownJavaType: Type = TypeFactory.unknownType()

internal val unknownTypeScriptType: TypeScriptType = TypeScriptUnknown(unknownJavaType)
