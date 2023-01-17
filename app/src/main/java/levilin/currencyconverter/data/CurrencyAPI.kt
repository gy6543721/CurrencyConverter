package levilin.currencyconverter.data

import levilin.currencyconverter.model.*
import levilin.currencyconverter.utility.ConstantValue
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface CurrencyAPI {

    // Currency Code
    @GET("api/currencies.json")
    suspend fun getCurrencyCode(): Response<CurrencyCode>

    // Exchange Rate
    @GET("api/latest.json")
    suspend fun getCurrencyExchangeRate(@QueryMap queries: Map<String, String>): Response<CurrencyData>


}