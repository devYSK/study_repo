package com.yscorp.dgsweb.fake

import com.yscorp.dgsweb.codegen.types.Hello
import jakarta.annotation.PostConstruct
import net.datafaker.Faker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataFakerConfig {

    @Bean
    fun dataFaker(): Faker {
        return Faker()
    }

    @PostConstruct
    fun postConstruct() {
        repeat(20) {
            val hello = Hello(
                { dataFaker().company().name() },

                { dataFaker().random().nextInt(5000) }
            )
            HELLO_LIST.add(hello)
        }
    }

    companion object {
        val HELLO_LIST: MutableList<Hello> = mutableListOf()

    }


}