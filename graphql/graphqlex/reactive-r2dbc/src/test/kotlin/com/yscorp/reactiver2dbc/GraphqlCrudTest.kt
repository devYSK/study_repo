package com.yscorp.reactiver2dbc

import com.yscorp.reactiver2dbc.dto.DeleteResponseDto
import com.yscorp.reactiver2dbc.dto.Status
import com.yscorp.reactiver2dbc.infra.client.CustomerDto
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.HttpGraphQlTester
import org.springframework.test.context.TestPropertySource
import kotlin.test.Test

@SpringBootTest
@AutoConfigureHttpGraphQlTester
@TestPropertySource(properties = ["lec=lec16"])
class GraphqlCrudTest {

    @Autowired
    lateinit var client: HttpGraphQlTester

    @Test
    fun allCustomersTest() {
        val doc = """
            query {
                customers {
                    name
                    age
                }
            }
        """
        client.document(doc)
            .execute()
            .path("customers").entityList(Any::class.java).hasSizeGreaterThan(2)
            .path("customers.[0].name").entity(String::class.java).isEqualTo("sam")
    }

    @Test
    fun customerByIdTest() {
        client.documentName("crud-operations")
            .variable("id", 1)
            .operationName("GetCustomerById")
            .execute()
            .path("response.id").entity(Int::class.java).isEqualTo(1)
            .path("response.name").entity(String::class.java).isEqualTo("sam")
            .path("response.age").entity(Int::class.java).isEqualTo(10)
    }

    @Test
    fun createCustomerTest() {
        client.documentName("crud-operations")
            .variable("customer", CustomerDto(name = "michael", age = 55, city = "seattle"))
            .operationName("CreateCustomer")
            .execute()
            .path("response.id").entity(Int::class.java).isEqualTo(5)
            .path("response.name").entity(String::class.java).isEqualTo("michael")
            .path("response.age").entity(Int::class.java).isEqualTo(55)
    }

    @Test
    fun updateCustomerTest() {
        client.documentName("crud-operations")
            .variable("id", 2)
            .variable("customer", CustomerDto(name = "obie", age = 45, city = "dallas"))
            .operationName("UpdateCustomer")
            .execute()
            .path("response.id").entity(Int::class.java).isEqualTo(2)
            .path("response.name").entity(String::class.java).isEqualTo("obie")
            .path("response.city").entity(String::class.java).isEqualTo("dallas")
            .path("response").entity(Any::class.java).satisfies { println(it) }
    }

    @Test
    fun deleteCustomerTest() {
        client.documentName("crud-operations")
            .variable("id", 3)
            .operationName("DeleteCustomer")
            .execute()
            .path("response").entity(DeleteResponseDto::class.java).satisfies { r ->
                Assertions.assertThat(r.id).isEqualTo(3)
                Assertions.assertThat(r.status).isEqualTo(Status.SUCCESS)
            }
    }
}
