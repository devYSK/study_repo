package com.yscorp.reactiver2dbc.infra.client

import com.yscorp.reactiver2dbc.dto.CustomerEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.graphql.client.WebSocketGraphQlClient
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Flux

class SubscriptionClient(
    @Value("\${customer.events.subscription.url}") private val baseUrl: String
) {
    private val client = WebSocketGraphQlClient.builder(baseUrl, ReactorNettyWebSocketClient())
        .build()

    // GraphQL subscription query
    private val customerEventsQuery = """
        subscription {
            customerEvents {
                id
                action
            }
        }
    """.trimIndent()

    fun customerEvents(): Flux<CustomerEvent> {
        return client.document(customerEventsQuery)
            .retrieveSubscription("customerEvents")
            .toEntity(CustomerEvent::class.java)
    }
}

// graphql-kotlin을 이용

//import com.expediagroup.graphql.client.GraphQLClient
//import com.expediagroup.graphql.client.types.GraphQLResponse
//import com.expediagroup.graphql.client.types.Subscription
//import reactor.core.publisher.Flux
//
//class SubscriptionClient(
//    @Value("\${customer.events.subscription.url}") private val baseUrl: String
//) {
//    private val client = GraphQLClient(baseUrl)
//
//    // Define GraphQL subscription using Kotlin class
//    class CustomerEventsSubscription : Subscription<CustomerEvent>("customerEvents")
//
//    fun customerEvents(): Flux<CustomerEvent> {
//        val subscription = CustomerEventsSubscription()
//        return Flux.create { sink ->
//            client.execute(subscription) { response: GraphQLResponse<CustomerEvent> ->
//                response.data?.let { sink.next(it) }
//                response.errors?.forEach { sink.error(RuntimeException(it.message)) }
//            }
//        }
//    }
//}