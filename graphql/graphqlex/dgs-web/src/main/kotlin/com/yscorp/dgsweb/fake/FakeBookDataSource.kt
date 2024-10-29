package com.yscorp.dgsweb.fake


import com.yscorp.dgsweb.codegen.types.Address
import com.yscorp.dgsweb.codegen.types.Author
import com.yscorp.dgsweb.codegen.types.Book
import com.yscorp.dgsweb.codegen.types.ReleaseHistory
import jakarta.annotation.PostConstruct
import net.datafaker.Faker
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Autowired
import kotlin.random.Random

@Configuration
class FakeBookDataSource {

    @Autowired
    private lateinit var faker: Faker

    companion object {
        val BOOK_LIST: MutableList<Book> = mutableListOf()
    }


    @PostConstruct
    private fun postConstruct() {
        val a: Int
        for (i in 0 until 20) {
            val addresses = mutableListOf<Address>()
            val author = Author(
                name = { faker.book().author() },
                originCountry = { faker.country().name() },
                addresses = { addresses }
            )

            val released = ReleaseHistory(
                printedEdition = { faker.bool().bool() },
                releasedCountry = { faker.country().name() },
                year = { faker.number().numberBetween(2019, 2021) }
            )

            val book = Book(
                title = { faker.book().title() },
                publisher = { faker.book().publisher() },
                author = { author },
                released = { released }
            )

            for (j in 0 until Random.nextInt(1, 3)) {
                val address = Address(
                    country = { faker.address().country() },
                    city = { faker.address().cityName() },
                    street = { faker.address().streetAddress() },
                    zipCode = { faker.address().zipCode() }
                )
                addresses.add(address)
            }

            BOOK_LIST.add(book)
        }
    }
}
