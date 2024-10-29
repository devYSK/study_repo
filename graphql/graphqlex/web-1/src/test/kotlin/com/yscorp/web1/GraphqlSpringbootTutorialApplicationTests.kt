package com.yscorp.web1

import com.yscorp.web1.common.Response
import com.yscorp.web1.common.Status
import com.yscorp.web1.domain.MemberType
import graphql.Assert
import graphql.ErrorType
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.ResponseError
import org.springframework.graphql.test.tester.GraphQlTester
import org.springframework.graphql.test.tester.HttpGraphQlTester
import org.springframework.graphql.test.tester.WebSocketGraphQlTester
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import org.springframework.web.reactive.socket.client.WebSocketClient
import reactor.test.StepVerifier
import java.time.Duration
import java.util.*


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = [Web1Application::class])
internal class GraphqlSpringbootTutorialApplicationTests {
    @Test
    fun testGraphqlQuery() {
//		String query = ""
//				+ "{"
//				+ "  secondQuery(firstName: \"test_first\", lastName: \"test_last\")"
//				+ "}";
        val response: String = httpTester!!.documentName("secondQuery")
            .execute()
            .path("secondQuery")
            .entity(String::class.java)
            .matches { p: String -> p.contains("test_first test_last") }
            .get().toString()

        Assert.assertNotNull(response).contains("test_first test_last")
    }

    @Test
    fun testGraphqlQueryWithInterceptor() {
        val query = ("{"
            + "  queryWithInterceptor"
            + "}")

        val modifiedTester = httpTester!!.mutate()
            .headers { header: HttpHeaders ->
                header["testHeader"] = "context value"
            }
            .build()

        modifiedTester.document(query)
            .execute()
            .path("queryWithInterceptor")
            .entity(String::class.java)
            .matches { p: String -> p.contains("Hello context value") }
    }

    @Test
    fun testMutation() {
        val addMemberResponse = httpTester!!.documentName("addMember")
            .variable("fn", "test_fn")
            .variable("ln", "test_ln")
            .variable("type", MemberType.TEACHER)
            .variable("contact", "test@example.com")
            .execute()
            .path("addMember")
            .entity(Response::class.java)
            .get()

        Assert.assertNotNull<Any>(addMemberResponse)
        Assert.assertNotNull(addMemberResponse.status) == Status.SUCCESS
        Assert.assertTrue(addMemberResponse.memberId > 0)

        httpTester!!.documentName("removeMember")
            .variable("id", addMemberResponse.memberId)
            .execute()
            .path(
                "removeMember"
            ) { np: GraphQlTester.Path ->
                np
                    .path("status").entity(Status::class.java).isEqualTo(Status.SUCCESS)
                    .path("memberId").entity(Int::class.java).isEqualTo(addMemberResponse.memberId)
                    .path("message").entity(String::class.java).matches { p -> p.contains("Member Removed") }
            }
    }

    @Test
    fun testMemberSubscription() {
        wsTester!!.start()

        val stream = wsTester!!.documentName("memberSubs")
            .executeSubscription()
            .toFlux("memberSubscription.message", String::class.java)


        // event -> addMember /remove
        Timer().schedule(
            object : TimerTask() {
                override fun run() {
                    val addMemberResponse: Response = httpTester!!.documentName("addMember")
                        .variable("fn", "test_fn_subs")
                        .variable("ln", "test_ln_subs")
                        .variable("type", MemberType.TEACHER)
                        .variable("contact", "test@example.com")
                        .execute()
                        .path("addMember")
                        .entity(Response::class.java)
                        .get()

                    Assert.assertNotNull<Any>(addMemberResponse)
                    Assert.assertNotNull(addMemberResponse.status) == Status.SUCCESS
                    Assert.assertTrue(addMemberResponse.memberId > 0)

                    httpTester!!.documentName("removeMember")
                        .variable("id", addMemberResponse.memberId)
                        .execute()
                        .path(
                            "removeMember"
                        ) { np: GraphQlTester.Path ->
                            np
                                .path("status").entity(Status::class.java).isEqualTo(Status.SUCCESS)
                                .path("memberId").entity(Int::class.java).isEqualTo(addMemberResponse.memberId)
                                .path("message").entity(String::class.java)
                                .matches { p -> p.contains("Member Removed") }
                        }
                }
            }, 2000
        )


        // wait for data to come in stream and verify
        StepVerifier.create(stream)
            .expectNext("Member Created: test_fn_subs test_ln_subs")
            .expectNext("Member Removed: test_fn_subs test_ln_subs")
            .thenCancel()
            .verify(Duration.ofSeconds(5))

        wsTester!!.stop()
    }

    @Test
    fun testGraphQLErrorForContextValue() {
        val query = ("{"
            + "  queryWithInterceptor"
            + "}")

        httpTester!!.document(query)
            .execute()
            .errors()
            .expect { error: ResponseError ->
                error.message!!.contains("Missing required context value")
            }
    }

    @Test
    fun testGraphQLValidationError() {
        httpTester!!.documentName("addMember")
            .variable("fn", "test_fn")
            .variable("ln", "test_ln")
            .variable("type", MemberType.TEACHER)
            .variable("contact", "testexample.com")
            .execute()
            .errors()
            .expect { error: ResponseError -> error.errorType == ErrorType.ValidationError }
    }

    companion object {
        private var httpTester: HttpGraphQlTester? = null
        private var wsTester: WebSocketGraphQlTester? = null

        @BeforeAll
        fun init(@Autowired context: WebApplicationContext) {
            val webClient = MockMvcWebTestClient.bindToApplicationContext(context!!)
                .configureClient()
                .baseUrl("/apis")
                .build()
            httpTester = HttpGraphQlTester.create(webClient)

            val wsClient: WebSocketClient = ReactorNettyWebSocketClient()
            val url = "ws://localhost:8080/subscription"
            wsTester = WebSocketGraphQlTester.builder(url, wsClient).build()
        }
    }

}
