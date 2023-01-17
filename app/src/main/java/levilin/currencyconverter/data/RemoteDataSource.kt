package levilin.currencyconverter.data

import levilin.currencyconverter.model.CurrencyCode
import levilin.currencyconverter.model.CurrencyExchangeRate
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val currencyAPI: CurrencyAPI) {

    // Currency Code
    suspend fun getCurrencyCode(): Response<CurrencyCode> {
        return currencyAPI.getCurrencyCode()
    }

    // Exchange Rate
    suspend fun getCurrencyExchangeRate(queries: Map<String, String>): Response<CurrencyExchangeRate> {
        return currencyAPI.getCurrencyExchangeRate(queries = queries)
    }

    // Exchange Rate Free
    suspend fun getCurrencyExchangeRateFree(app_id: String): Response<CurrencyExchangeRate> {
        return currencyAPI.getCurrencyExchangeRateFree(app_id = app_id)
    }
}