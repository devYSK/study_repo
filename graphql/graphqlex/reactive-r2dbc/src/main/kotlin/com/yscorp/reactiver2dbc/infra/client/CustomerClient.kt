package com.yscorp.reactiver2dbc.infra.client


import com.yscorp.reactiver2dbc.dto.DeleteResponseDto
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.graphql.client.ClientGraphQlResponse
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

//@Service
class CustomerClient(
    @Value("\${customer.service.url}") baseUrl: String,
) {

    private val client: HttpGraphQlClient = HttpGraphQlClient.builder()
        .webClient { it.baseUrl(baseUrl) }
        .build()

    fun rawQuery(query: String): Mono<ClientGraphQlResponse> {
        return client.document(query)
            .execute()
    }

    suspend fun getCustomerById(id: Int): GenericResponse<CustomerDto?>? {
        return client
            .documentName("customer-by-id")
            .variable("id", id)
            .execute()
            .map { cr ->
                val field = cr.field("customerById")

                if (field.getValue<String>() != null) {
                    GenericResponse(field.toEntity(CustomerDto::class.java))
                } else {
                    GenericResponse(field.errors)
                }
            }.awaitSingleOrNull()
    }

    suspend fun getCustomerByIdWithUnion(id: Int): CustomerResponse? {
        return client.documentName("customer-by-id")
            .variable("id", id)
            .execute()
            .map { cr ->
                val field = cr.field("customerById")
                val isCustomer = cr.field("customerById.type").getValue<String>().toString() == "Customer"
                if (isCustomer) {
                    field.toEntity(CustomerDto::class.java)
                } else {
                    field.toEntity(CustomerNotFound::class.java)
                }
            }.awaitSingleOrNull()
    }

    fun allCustomers(): Mono<List<CustomerDto>> {
        return crud("GetAll", emptyMap(), object : ParameterizedTypeReference<List<CustomerDto>>() {})
    }

    fun customerById(id: Int): Mono<CustomerDto> {
        return crud("GetCustomerById", mapOf("id" to id), object : ParameterizedTypeReference<CustomerDto>() {})
    }

    fun createCustomer(dto: CustomerDto): Mono<CustomerDto> {
        return crud("CreateCustomer", mapOf("customer" to dto), object : ParameterizedTypeReference<CustomerDto>() {})
    }

    fun updateCustomer(id: Int, dto: CustomerDto): Mono<CustomerDto> {
        return crud(
            "UpdateCustomer",
            mapOf("id" to id, "customer" to dto),
            object : ParameterizedTypeReference<CustomerDto>() {})
    }

    fun deleteCustomer(id: Int): Mono<DeleteResponseDto> {
        return crud("DeleteCustomer", mapOf("id" to id), object : ParameterizedTypeReference<DeleteResponseDto>() {})
    }

    private fun <T> crud(
        operationName: String,
        variables: Map<String, Any>,
        type: ParameterizedTypeReference<T>,
    ): Mono<T> {
        return client.documentName("crud-operations")
            .operationName(operationName)
            .variables(variables)
            .retrieve("response")
            .toEntity(type)
    }

}