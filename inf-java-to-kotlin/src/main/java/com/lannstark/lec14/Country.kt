package com.lannstark.lec14

/**
 *
 * @author   : ysk
 */
enum class Country(
    val code: String
) {

    KOREA("KO"),
    AMERICA("US"),
    ;
}


private fun handleCountry(country: Country) {
    when (country) {
        Country.KOREA -> TODO()
        Country.AMERICA -> TODO()
    }
}