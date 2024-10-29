package com.yscorp.dgsweb.exception

import com.netflix.graphql.dgs.exceptions.DefaultDataFetcherExceptionHandler
import com.netflix.graphql.types.errors.ErrorType
import com.netflix.graphql.types.errors.TypedGraphQLError
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture


@Component
class ProblemzGraphqlExceptionHandler : DataFetcherExceptionHandler {
    private val defaultHandler = DefaultDataFetcherExceptionHandler()

    override fun handleException(
        handlerParameters: DataFetcherExceptionHandlerParameters,
    ): CompletableFuture<DataFetcherExceptionHandlerResult> {
        val exception = handlerParameters.exception

        if (exception is ProblemzAuthenticationException) {
            val graphqlError = TypedGraphQLError.newBuilder().message(exception.message)
                .path(handlerParameters.path) //                    .errorType(ErrorType.UNAUTHENTICATED)
                .errorDetail(ProblemzErrorDetail())
                .build()

            val result = DataFetcherExceptionHandlerResult.newResult()
                .error(graphqlError).build()

            return CompletableFuture.completedFuture(result)
        } else if (exception is ProblemzPermissionException) {
            val graphqlError = TypedGraphQLError.newBuilder().message(exception.message)
                .path(handlerParameters.path)
                .errorType(ErrorType.PERMISSION_DENIED)
                .build()

            val result = DataFetcherExceptionHandlerResult.newResult()
                .error(graphqlError).build()

            return CompletableFuture.completedFuture(result)
        }

        return defaultHandler.handleException(handlerParameters)
    }

}
