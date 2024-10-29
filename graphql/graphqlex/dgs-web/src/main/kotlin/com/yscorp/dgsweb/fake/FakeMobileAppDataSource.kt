package com.yscorp.dgsweb.fake

import com.yscorp.dgsweb.codegen.types.Address
import com.yscorp.dgsweb.codegen.types.Author
import com.yscorp.dgsweb.codegen.types.MobileApp
import com.yscorp.dgsweb.codegen.types.MobileAppCategory
import jakarta.annotation.PostConstruct
import net.datafaker.Faker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import java.net.MalformedURLException
import java.net.URI
import java.time.LocalDate
import java.util.*
import kotlin.random.Random

@Configuration
class FakeMobileAppDataSource {

    companion object {
        val MOBILE_APP_LIST: MutableList<MobileApp> = mutableListOf()
    }

    @Autowired
    private lateinit var faker: Faker

    @PostConstruct
    @Throws(MalformedURLException::class)
    private fun postConstruct() {
        repeat(20) {
            val addresses = mutableListOf<Address>()
            val author = Author(
                name = { faker.app().author() },
                originCountry = { faker.country().name() },
                addresses = { addresses }
            )
            val mobileApp = MobileApp(
                name = { faker.app().name() },
                author = { author },
                version = { faker.app().version() },
                platform = { randomMobileAppPlatform() },
                appId = { UUID.randomUUID().toString() },
                releaseDate = { LocalDate.now().minusDays(faker.random().nextInt(365).toLong()) },
                downloaded = { faker.number().numberBetween(1, 1_500_000) },
                homepage = { URI.create("https://" + faker.internet().url()).toURL() },
                category = { MobileAppCategory.values()[faker.random().nextInt(MobileAppCategory.values().size)] }
            )

            repeat(Random.nextInt(1, 3)) {
                val address = Address(
                    country = { faker.address().country() },
                    city = { faker.address().cityName() },
                    street = { faker.address().streetAddress() },
                    zipCode = { faker.address().zipCode() }
                )
                addresses.add(address)
            }

            MOBILE_APP_LIST.add(mobileApp)
        }
    }

    private fun randomMobileAppPlatform(): List<String> {
        return when (Random.nextInt(10) % 3) {
            0 -> listOf("android")
            1 -> listOf("ios")
            else -> listOf("ios", "android")
        }
    }
}
