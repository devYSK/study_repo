package com.accounts.com.accounts.directive;

import graphql.schema.*;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import org.springframework.stereotype.Component;


@Component
public class CaseDirective implements SchemaDirectiveWiring {

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        GraphQLFieldsContainer container = environment.getFieldsContainer();
        GraphQLFieldDefinition field = environment.getElement();
        DataFetcher<?> originalFetcher = environment.getCodeRegistry().getDataFetcher(container, field);

        // Determine the directive name (either "upper" or "lower")
        String directiveName = environment.getDirective().getName();
        DataFetcher<?> modifiedFetcher = createModifiedFetcher(originalFetcher, directiveName);

        // Register the new data fetcher with the code registry
        environment.getCodeRegistry().dataFetcher(container, field, modifiedFetcher);

        return field;
    }

    private DataFetcher<?> createModifiedFetcher(DataFetcher<?> originalFetcher, String directiveName) {
        return dataFetchingEnvironment -> {
            Object originalValue = originalFetcher.get(dataFetchingEnvironment);
            if (originalValue instanceof String) {
                return directiveName.equals("upper") ?
                        ((String) originalValue).toUpperCase() :
                        ((String) originalValue).toLowerCase();
            }
            return originalValue;
        };
    }
}
