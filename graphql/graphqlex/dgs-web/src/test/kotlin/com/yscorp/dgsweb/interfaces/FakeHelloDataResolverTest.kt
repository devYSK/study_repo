package com.yscorp.dgsweb.interfaces

import com.netflix.graphql.dgs.DgsQueryExecutor
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest
class FakeHelloDataResolverTest {
    @Autowired
    lateinit var dgsQueryExecutor: DgsQueryExecutor


    @Test
    fun testOneHello() {
        val query =
            """
            {
                oneHello {
                    text
                    randomNumber
                }
            }
        """.trimIndent()


        val text = dgsQueryExecutor.executeAndExtractJsonPath<String>(query, "data.oneHello.text")
        val randomNumber = dgsQueryExecutor.executeAndExtractJsonPath<Int>(query, "data.oneHello.randomNumber")

        Assertions.assertThat(text).isNotBlank
        Assertions.assertThat(randomNumber).isNotNull
        println("text: $text")
        println("randomNumber: $randomNumber")

    }

    @Test
    fun testAllHellos() {
        val query =
            """
            {
                allHellos {
                    text
                    randomNumber
                }
            }
        """.trimIndent()

        val data = dgsQueryExecutor.executeAndExtractJsonPath<List<String>>(query, "data.allHellos[*].text")

        Assertions.assertThat(data).isNotEmpty
        println("data: $data")
    }
}