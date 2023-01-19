package levilin.currencyconverter.data.remote

import levilin.currencyconverter.model.*
import levilin.currencyconverter.model.remote.CountryName
import levilin.currencyconverter.model.remote.CurrencyExchangeRate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CurrencyAPI {
    // Currency Code
    @GET("api/currencies.json")
    suspend fun getCurrencyCode(): Response<CountryName>

    // Currency Exchange Rate
    @GET("api/latest.json")
    suspend fun getCurrencyExchangeRate(@QueryMap queries: Map<String, String>): Response<CurrencyExchangeRate>
}