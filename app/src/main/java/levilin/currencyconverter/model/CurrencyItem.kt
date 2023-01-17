package levilin.currencyconverter.model

import java.io.Serializable

data class CurrencyItem(
    val countryName: String,
    val currencyCode: String
): Serializable