package levilin.currencyconverter.model

import levilin.currencyconverter.model.remote.Rates
import java.io.Serializable

data class CurrencyItem(
    val countryName: String,
    val currencyCode: String
): Serializable {
    fun convertOrderByCurrencyCode(): Int {
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

    fun convertRatesByCurrencyCode(rates: Rates): Double {
        return when(currencyCode) {
            "AUD" -> rates.aUD
            "BGN" -> rates.bGN
            "BRL" -> rates.bRL
            "CAD" -> rates.cAD
            "CHF" -> rates.cHF
            "CNY" -> rates.cNY
            "CZK" -> rates.cZK
            "DKK" -> rates.dKK
            "EUR" -> rates.eUR
            "GBP" -> rates.gBP
            "HKD" -> rates.hKD
            "HRK" -> rates.hRK
            "HUF" -> rates.hUF
            "IDR" -> rates.iDR
            "INR" -> rates.iNR
            "ISK" -> rates.iSK
            "JPY" -> rates.jPY
            "KRW" -> rates.kRW
            "MXN" -> rates.mXN
            "MYR" -> rates.mYR
            "NOK" -> rates.nOK
            "NZD" -> rates.nZD
            "PHP" -> rates.pHP
            "PLN" -> rates.pLN
            "RON" -> rates.rON
            "RUB" -> rates.rUB
            "SEK" -> rates.sEK
            "SGD" -> rates.sGD
            "THB" -> rates.tHB
            "TRY" -> rates.tRY
            "TWD" -> rates.tWD
            "USD" -> rates.uSD
            "ZAR" -> rates.zAR
            else -> 1.00
        }
    }
}