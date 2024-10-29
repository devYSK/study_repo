package com.yscorp.ex1.interceptor

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import org.springframework.graphql.ResponseError
import org.springframework.graphql.execution.ErrorType
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.stream.Collectors


@Component
class RequestErrorInterceptor : WebGraphQlInterceptor {
    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        return chain.next(request)
            .map { response: WebGraphQlResponse ->
                this.processResponse(
                    response
                )
            }
    }

    private fun processResponse(response: WebGraphQlResponse): WebGraphQlResponse {
        if (response.isValid) {
            return response
        } else {
            val modifiedErrors = modifyErrors(response.errors)
            return response.transform { builder: WebGraphQlResponse.Builder ->
                builder.errors(
                    modifiedErrors
                ).build()
            }
        }
    }

    private fun modifyErrors(originalErrors: List<ResponseError>): List<GraphQLError> {
        return originalErrors.stream()
            .map { error: ResponseError ->
                this.createValidationError(
                    error
                )
            }
            .collect(Collectors.toList())
    }

    private fun createValidationError(error: ResponseError): GraphQLError {
        var errorMessage: String? = null
        val extensionMap: MutableMap<String, Any> = HashMap()
        if (error.message!!.contains("is not a valid 'CountryCode'")) {
            errorMessage = "Invalid country code. Use a supported country code."
            extensionMap["Supported Country Codes"] =
                "International country codes are short alphanumeric combinations that uniquely identify countries or geographical areas around the world. "
        } else if (error.message!!.contains("is not a valid 'Currency'")) {
            errorMessage = "Invalid Currency code. Use a supported Currency code."
            extensionMap["Supported Currency Codes"] = "USD, CAD, EUR"
        }

        return GraphqlErrorBuilder.newError()
            .message(errorMessage)
            .errorType(ErrorType.BAD_REQUEST)
            .extensions(extensionMap)
            .locations(error.locations) // Add more customization to the error as needed
            .build()
    }
}
