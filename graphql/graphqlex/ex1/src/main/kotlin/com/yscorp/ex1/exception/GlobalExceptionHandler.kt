package com.yscorp.ex1.exception

import graphql.GraphQLError
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler
import org.springframework.graphql.execution.ErrorType
import org.springframework.web.bind.annotation.ControllerAdvice
import java.time.Instant
import javax.security.auth.login.AccountNotFoundException


@ControllerAdvice
class GlobalExceptionHandler {
    var extMap: MutableMap<String, Any> = HashMap()

    @GraphQlExceptionHandler
    fun handle( ex: AccountNotFoundException, environment: DataFetchingEnvironment): GraphQLError {
        extMap["errorCode"] = "ACCOUNT_NOT_FOUND"
        extMap["userMessage"] = "The account you are trying to access does not exist."
        extMap["timestamp"] = Instant.now().toString()
        extMap["actionableSteps"] = "Please verify the account ID and try again."

        return GraphQLError
            .newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message("Sorry, we couldn't find the requested account. Please check the account ID and try again : " + ex.message)
            .path(environment.executionStepInfo.path)
            .location(environment.field.sourceLocation)
            .extensions(extMap)
            .build()
    }
}
