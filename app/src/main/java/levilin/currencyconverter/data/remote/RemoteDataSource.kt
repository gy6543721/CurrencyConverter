package levilin.currencyconverter.data.remote

import levilin.currencyconverter.model.remote.CountryName
import levilin.currencyconverter.model.remote.CurrencyExchangeRate
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val currencyAPI: CurrencyAPI) {
    // Currency Code
    suspend fun getCurrencyCode(): Response<CountryName> {
        return currencyAPI.getCurrencyCode()
    }

    // Currency Exchange Rate
    suspend fun getCurrencyExchangeRate(queries: Map<String, String>): Response<CurrencyExchangeRate> {
        return currencyAPI.getCurrencyExchangeRate(queries = queries)
    }
}