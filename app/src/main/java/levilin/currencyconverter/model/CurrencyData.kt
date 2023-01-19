package levilin.currencyconverter.model

import androidx.room.Entity
import java.io.Serializable

//@Entity
data class CurrencyData(
    val id: Int,
    val countryName: String,
    val currencyCode: String,
    val currencyExchangeRate: Double,
    var convertedValue: String
): Serializable

fun CurrencyData.toOrderByCurrencyCode(): Int {
    return when(currencyCode) {
        "AUD" -> 0
        "BGN" -> 1
        "BRL" -> 2
        "CAD" -> 3
        "CHF" -> 4
        "CNY" -> 5
        "CZK" -> 6
        "DKK" -> 7
        "EUR" -> 8
        "GBP" -> 9
        "HKD" -> 10
        "HRK" -> 11
        "HUF" -> 12
        "IDR" -> 13
        "INR" -> 14
        "ISK" -> 15
        "JPY" -> 16
        "KRW" -> 17
        "MXN" -> 18
        "MYR" -> 19
        "NOK" -> 20
        "NZD" -> 21
        "PHP" -> 22
        "PLN" -> 23
        "RON" -> 24
        "RUB" -> 25
        "SEK" -> 26
        "SGD" -> 27
        "THB" -> 28
        "TRY" -> 29
        "TWD" -> 30
        "USD" -> 31
        "ZAR" -> 32
        else -> 33
    }
}

