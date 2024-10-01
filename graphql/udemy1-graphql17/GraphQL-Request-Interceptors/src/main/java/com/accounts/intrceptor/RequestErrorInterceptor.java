package com.accounts.intrceptor;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import org.springframework.graphql.ResponseError;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RequestErrorInterceptor implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        return chain.next(request)
                .map (this::processResponse);
    }

    private WebGraphQlResponse processResponse(WebGraphQlResponse response) {
        if (response.isValid()){
            return response;
        }else{
            List<GraphQLError> modifiedErrors = modifyErrors(response.getErrors());
            return response.transform(builder -> builder.errors(modifiedErrors).build());
        }
    }

    private List<GraphQLError> modifyErrors(List<ResponseError> originalErrors) {
        return originalErrors.stream()
                .map(this::createValidationError)
                .collect(Collectors.toList());
    }

    private GraphQLError createValidationError(ResponseError error) {
        String errorMessage = null;
        Map<String, Object> extensionMap = new HashMap<>();
        if (error.getMessage().contains("is not a valid 'CountryCode'")){
            errorMessage = "Invalid country code. Use a supported country code.";
            extensionMap.put("Supported Country Codes", "International country codes are short alphanumeric combinations that uniquely identify countries or geographical areas around the world. ");
        }else if (error.getMessage().contains("is not a valid 'Currency'")){
            errorMessage = "Invalid Currency code. Use a supported Currency code.";
            extensionMap.put("Supported Currency Codes", "USD, CAD, EUR");
        }

        return GraphqlErrorBuilder.newError()
                .message(errorMessage)
                .errorType(ErrorType.BAD_REQUEST)
                .extensions(extensionMap)
                .locations(error.getLocations())
                // Add more customization to the error as needed
                .build();
    }
}
