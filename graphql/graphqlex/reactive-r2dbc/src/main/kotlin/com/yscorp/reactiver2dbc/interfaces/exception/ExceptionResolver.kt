package com.yscorp.reactiver2dbc.interfaces.exception

import graphql.ExecutionInput
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolver
import org.springframework.graphql.execution.ErrorType
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.Map

@Service
class ExceptionResolver : DataFetcherExceptionResolver {
    override fun resolveException(
        exception: Throwable,
        environment: DataFetchingEnvironment,
    ): Mono<List<GraphQLError>> {
        val ex = toApplicationException(exception)
        return Mono.just(
            java.util.List.of(
                GraphqlErrorBuilder.newError()
                    .message(ex.message)
                    .errorType(ex.errorType)
                    .extensions(ex.extensions)
                    .build()
            )
        )
    }

    private fun toApplicationException(throwable: Throwable): ApplicationException {
        return if (ApplicationException::class.java == throwable.javaClass) throwable as ApplicationException else ApplicationException(
            ErrorType.INTERNAL_ERROR, throwable.message ?: "error", Map.of()
        )
    }
}


@Service
class RequestInterceptor : WebGraphQlInterceptor {
    // client has to pass caller-id
    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        val headers = request.headers.getOrEmpty("caller-id")

        val callerId = if (headers.isEmpty()) "" else headers[0]
        request.configureExecutionInput { e: ExecutionInput?, b: ExecutionInput.Builder ->
            b.graphQLContext(
                Map.of<String?, Any>("caller-id", callerId)
            ).build()
        }

        return chain.next(request)
    }
}
