package com.yscorp.reactiver2dbc

import com.yscorp.reactiver2dbc.interfaces.graphql.Electronics
import com.yscorp.reactiver2dbc.interfaces.graphql.FruitDto
import com.yscorp.reactiver2dbc.interfaces.graphql.MyBook
import graphql.scalars.ExtendedScalars
import graphql.schema.TypeResolver
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer

@Configuration
class ScalarConfig {

    @Bean
    fun configurer(): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { c: RuntimeWiring.Builder ->
            c.scalar(ExtendedScalars.GraphQLLong)
                .scalar(ExtendedScalars.GraphQLByte)
                .scalar(ExtendedScalars.GraphQLShort)
                .scalar(ExtendedScalars.GraphQLBigDecimal)
                .scalar(ExtendedScalars.GraphQLBigInteger)
                .scalar(ExtendedScalars.Date)
                .scalar(ExtendedScalars.LocalTime)
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.Object)
                .type("IProduct") { builder ->
                    builder.typeResolver { env ->
                        val obj = env.getObject<Any>()
                        when (obj) {
                            is FruitDto -> env.schema.getObjectType("Fruit")
                            is Electronics -> env.schema.getObjectType("Electronics")
                            is MyBook -> env.schema.getObjectType("Book")
                            else -> null
                        }
                    }
                }
        }
    }

}
