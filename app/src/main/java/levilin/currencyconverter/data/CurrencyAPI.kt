package levilin.currencyconverter.data

import levilin.currencyconverter.model.*
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CurrencyAPI {
    // Currency Abbreviation
    @GET("api/currencies.json")
    suspend fun getCurrencies(@QueryMap queries: Map<String, String>): CurrencyAbbreviation
}