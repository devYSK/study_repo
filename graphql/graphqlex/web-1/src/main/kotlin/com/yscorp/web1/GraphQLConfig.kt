package com.yscorp.web1


import graphql.Scalars
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import graphql.schema.idl.RuntimeWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import java.util.regex.Pattern

@Configuration
class GraphQLConfig {
    var emailCustomScalar: GraphQLScalarType = ExtendedScalars
        .newRegexScalar("Email")
        .description("Custom Scalar for Email Address")
        .addPattern(Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
        .build()

    var keyAliasScalar: GraphQLScalarType = ExtendedScalars
        .newAliasedScalar("Key")
        .description("Key for ID scalar")
        .aliasedScalar(Scalars.GraphQLID)
        .build()

    var CursorStringScalar: GraphQLScalarType = ExtendedScalars
        .newAliasedScalar("CursorString")
        .description("Cursor scalar type")
        .aliasedScalar(Scalars.GraphQLID)
        .build()

    @Bean
    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { wiringBuilder: RuntimeWiring.Builder ->
            wiringBuilder
                .scalar(ExtendedScalars.GraphQLBigDecimal)
                .scalar(emailCustomScalar)
                .scalar(keyAliasScalar)
                .scalar(CursorStringScalar)
                .directive("uppercase", UppercaseSchemaDirective())
                .build()
        }
    }
}
