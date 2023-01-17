package levilin.currencyconverter.model

import java.io.Serializable

data class CurrencyData(
    val countryName: String,
    val currencyCode: String,
    val currencyExchangeRate: Double
): Serializable