package com.accounts.exceptions;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import lombok.NonNull;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    Map<String, Object> extMap = new HashMap<>();

    @GraphQlExceptionHandler
    public GraphQLError handle(@NonNull AccountNotFoundException ex, @NonNull DataFetchingEnvironment environment) {
        extMap.put("errorCode", "ACCOUNT_NOT_FOUND");
        extMap.put("userMessage", "The account you are trying to access does not exist.");
        extMap.put("timestamp", Instant.now().toString());
        extMap.put("actionableSteps", "Please verify the account ID and try again.");

        return GraphQLError
                .newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message("Sorry, we couldn't find the requested account. Please check the account ID and try again : " + ex.getMessage())
                .path(environment.getExecutionStepInfo().getPath())
                .location(environment.getField().getSourceLocation())
                .extensions(extMap)
                .build();
    }
}
