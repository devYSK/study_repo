package com.yscorp.reactiver2dbc.infra.client

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service

@Service
class ClientDemo(
    private val client: CustomerClient,
    private val subscriptionClient: SubscriptionClient,
) : CommandLineRunner {

    override fun run(vararg args: String?) = runBlocking {
//        subscriptionClient.customerEvents()
//            .collect { event -> println(" ** $event ** ") }

        allCustomersDemo()
        customerByIdDemo()
        createCustomerDemo()
        updateCustomerDemo()
        deleteCustomerDemo()
    }

    private suspend fun rawQueryDemo() {
        val query = """
            {
                a: customers {
                    id
                    name
                    age
                    city
                }
            }
        """.trimIndent()

        val result = client.rawQuery(query)
            .map { cr -> cr.field("a").toEntityList(CustomerDto::class.java) }

        executor("Raw Query", result)
    }

    private suspend fun getCustomerById() {
        executor("getCustomerById", client.getCustomerByIdWithUnion(5))
    }

    private suspend fun allCustomersDemo() {
        executor("allCustomersDemo", client.allCustomers())
    }

    private suspend fun customerByIdDemo() {
        executor("customerByIdDemo", client.customerById(11))
    }

    private suspend fun createCustomerDemo() {
        executor("createCustomerDemo", client.createCustomer(CustomerDto(name = "obie", age = 45, city = "detroit")))
    }

    private suspend fun updateCustomerDemo() {
        executor(
            "updateCustomerDemo", client.updateCustomer(
                2,
                CustomerDto(name = "jackson", age = 15, city = "dallas")
            )
        )
    }

    private suspend fun deleteCustomerDemo() {
        executor("deleteCustomerDemo", client.deleteCustomer(3))
    }

    private suspend fun <T> executor(message: String, result: T) {
        delay(1000)  // Mono.delay(Duration.ofSeconds(1)) 대신
        println(message)
        println(result)
    }

}