package levilin.currencyconverter.data.remote

import levilin.currencyconverter.model.remote.CountryName
import levilin.currencyconverter.model.remote.CurrencyExchangeRate
import retrofit2.Response


class FakeCurrencyAPI: CurrencyAPI {

    override suspend fun getCurrencyCode(): Response<CountryName> {
        val countryNameResponseBody = CountryName()
        return Response.success(countryNameResponseBody)
    }

    override suspend fun getCurrencyExchangeRate(queries: Map<String, String>): Response<CurrencyExchangeRate> {
        val currencyExchangeRateBody = CurrencyExchangeRate()
        return Response.success(currencyExchangeRateBody)
    }
}