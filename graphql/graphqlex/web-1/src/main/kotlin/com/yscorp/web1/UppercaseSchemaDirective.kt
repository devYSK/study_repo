package com.yscorp.web1

import graphql.language.StringValue
import graphql.schema.*
import graphql.schema.idl.SchemaDirectiveWiring
import graphql.schema.idl.SchemaDirectiveWiringEnvironment

class UppercaseSchemaDirective : SchemaDirectiveWiring {

	override fun onField(environment: SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition>): GraphQLFieldDefinition {
		val field = environment.element
		val parentType = environment.fieldsContainer
		val conditionValue = (environment.getAppliedDirective().getArgument("condition")
			.argumentValue.value as StringValue).value

		val existingDF = environment.codeRegistry
			.getDataFetcher(FieldCoordinates.coordinates(parentType, field), field)

		val updatedDF = DataFetcherFactories.wrapDataFetcher(existingDF) { datafetchingEnv, value ->
			if (value is String && value.contains(conditionValue)) {
				value.uppercase()
			} else {
				value
			}
		}

		environment.codeRegistry.dataFetcher(FieldCoordinates.coordinates(parentType, field), updatedDF)
		return field
	}
}
