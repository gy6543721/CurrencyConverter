package levilin.currencyconverter.data

import levilin.currencyconverter.model.CurrencyCode
import levilin.currencyconverter.model.CurrencyData
import retrofit2.Response
import retrofit2.http.QueryMap
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val currencyAPI: CurrencyAPI) {

    // Currency Code
    suspend fun getCurrencyCode(): Response<CurrencyCode> {
        return currencyAPI.getCurrencyCode()
    }

    // Exchange Rate
    suspend fun getCurrencyExchangeRate(queries: Map<String, String>): Response<CurrencyData> {
        return currencyAPI.getCurrencyExchangeRate(queries = queries)
    }
}