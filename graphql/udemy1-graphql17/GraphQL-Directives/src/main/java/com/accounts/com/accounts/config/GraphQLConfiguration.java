package com.accounts.com.accounts.config;


import com.accounts.com.accounts.directive.CaseDirective;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;


@Configuration
public class GraphQLConfiguration {

    @Autowired
    private CaseDirective caseDirective;

    @Bean
    public GraphQL graphQL() {
        return GraphQL.newGraphQL(buildSchema()).build();
    }

    private GraphQLSchema buildSchema() {
        String sdl = loadSchemaFile();
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .directive("upper", caseDirective)
                .directive("lower", caseDirective)
                .build();
    }

    private String loadSchemaFile() {
        Resource resource = new ClassPathResource("graphql/bank/query.graphqls");
        try {
            return new String(Files.readAllBytes(resource.getFile().toPath()));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read schema file", e);
        }
    }
}
