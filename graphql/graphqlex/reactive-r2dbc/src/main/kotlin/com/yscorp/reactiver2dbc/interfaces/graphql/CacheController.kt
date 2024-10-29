package com.yscorp.reactiver2dbc.interfaces.graphql

import graphql.GraphQL
import graphql.execution.preparsed.PreparsedDocumentEntry
import graphql.execution.preparsed.PreparsedDocumentProvider
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.execution.GraphQlSource.SchemaResourceBuilder
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

@Controller
class CacheController {
    @QueryMapping
    fun sayCache(@Argument("name") value: String): Mono<String> {
        return Mono.fromSupplier { "Hello $value" }
    }

}

// 카페인 캐시~~ 이거 쓰면 더 좋을듯
@Configuration
class OperationCachingConfig {
    /*
               request body
               exe doc
               parse
               validation
               exe doc

               suggestion:
               use variables along with operation mame
               카페인 캐시 선호 : https://github.com/ben-manes/caffeine

        */
    @Bean
    fun sourceBuilderCustomizer(provider: PreparsedDocumentProvider?): GraphQlSourceBuilderCustomizer {
        return GraphQlSourceBuilderCustomizer { c: SchemaResourceBuilder ->
            c.configureGraphQl { builder: GraphQL.Builder ->
                builder.preparsedDocumentProvider(
                    provider
                )
            }
        }
    }

    @Bean
    fun provider(): PreparsedDocumentProvider {
        val map: MutableMap<String, PreparsedDocumentEntry> = ConcurrentHashMap()

        return PreparsedDocumentProvider { executionInput, parseAndValidateFunction ->
            val documentEntry = map.computeIfAbsent(
                executionInput.query
            ) { key: String ->
                println("Not found : $key")
                val r = parseAndValidateFunction.apply(executionInput)
                println("Caching : $r")
                r
            }

            CompletableFuture.completedFuture(documentEntry)
        }
    }
}
