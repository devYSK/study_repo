package com.yscorp.reactiver2dbc

import com.yscorp.reactiver2dbc.dto.Action
import com.yscorp.reactiver2dbc.dto.CustomerEvent
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.HttpGraphQlTester
import org.springframework.graphql.test.tester.WebSocketGraphQlTester
import org.springframework.test.context.TestPropertySource
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.test.StepVerifier
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureHttpGraphQlTester
@TestPropertySource(properties = ["lec=lec14"])
class GraphqlSubscriptionTest {

    companion object {
        private const val WS_PATH = "ws://localhost:8080/graphql"
    }

    @Autowired
    lateinit var client: HttpGraphQlTester

    @Test
    fun subscriptionTest() {
        val websocketClient = WebSocketGraphQlTester
            .builder(WS_PATH, ReactorNettyWebSocketClient())
            .build()

        // delete a customer
        client.documentName("crud-operations")
            .operationName("DeleteCustomer")
            .variable("id", 1)
            .executeAndVerify() // there are no errors

        websocketClient.documentName("subscription-test")
            .executeSubscription()
            .toFlux("customerEvents", CustomerEvent::class.java)
            .take(1)
            .`as`(StepVerifier::create)
            .consumeNextWith { e ->
                Assertions.assertThat(e.action).isEqualTo(Action.DELETED)
            }
            .verifyComplete()
    }
}