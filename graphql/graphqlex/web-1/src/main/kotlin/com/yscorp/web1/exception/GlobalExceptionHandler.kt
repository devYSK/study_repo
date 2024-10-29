package com.yscorp.web1.exception


import com.yscorp.web1.common.Response
import com.yscorp.web1.common.Status
import graphql.GraphQLError
import graphql.schema.DataFetchingEnvironment
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler
import org.springframework.graphql.execution.ErrorType
import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
class GlobalExceptionHandler {

    private val log = KotlinLogging.logger {  }

    @GraphQlExceptionHandler
    fun handle(ex: ArithmeticException, dfe: DataFetchingEnvironment): GraphQLError {
        val map: MutableMap<String, Any> = HashMap()

        log.error(ex) {  "${ex.message}" }

        map["details"] = Response(Status.FAILURE, -1, "detailed exception reason")

        return GraphQLError.newError()
            .message("arithmetic exception caught!")
            .errorType(ErrorType.INTERNAL_ERROR)
            .location(dfe.field.sourceLocation)
            .path(dfe.executionStepInfo.path)
            .extensions(map)
            .build()
    }

    @GraphQlExceptionHandler
    fun handle(ex: NullPointerException?, dfe: DataFetchingEnvironment): GraphQLError {
        return GraphQLError.newError()
            .message("arithmetic exception caught!")
            .errorType(ErrorType.INTERNAL_ERROR)
            .location(dfe.field.sourceLocation)
            .path(dfe.executionStepInfo.path)
            .build()
    }

    @GraphQlExceptionHandler
    fun handle(ex: Throwable, dfe: DataFetchingEnvironment): GraphQLError {
        return GraphQLError.newError()
            .message(ex.localizedMessage)
            .errorType(ErrorType.INTERNAL_ERROR)
            .location(dfe.field.sourceLocation)
            .path(dfe.executionStepInfo.path)
            .build()
    }
}
