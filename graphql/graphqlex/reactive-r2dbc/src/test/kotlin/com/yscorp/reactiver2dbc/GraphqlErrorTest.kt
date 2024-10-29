package com.yscorp.reactiver2dbc

import com.yscorp.reactiver2dbc.infra.client.CustomerDto
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.execution.ErrorType
import org.springframework.graphql.test.tester.HttpGraphQlTester
import org.springframework.test.context.TestPropertySource
import kotlin.test.Test

@SpringBootTest
@AutoConfigureHttpGraphQlTester
@TestPropertySource(properties = ["lec=lec15"])
class GraphqlErrorTest {

    @Autowired
    lateinit var client: HttpGraphQlTester

    @Test
    fun createCustomerTest() {
        client
            .mutate().header("caller-id", "demo").build()
            .documentName("crud-operations")
            .variable("customer", CustomerDto(name = "michael", age = 15, city = "seattle"))
            .operationName("CreateCustomer")
            .execute()
            .errors().satisfy { list ->
                Assertions.assertThat(list).hasSize(1)
                Assertions.assertThat(list[0].errorType).isEqualTo(ErrorType.BAD_REQUEST)
            }
            .path("response").valueIsNull()

        /*
            {
                "errors": [...],
                "response": null
            }
        */
    }
}