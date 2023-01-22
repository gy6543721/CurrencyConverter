package levilin.currencyconverter.data.remote

import androidx.compose.runtime.mutableStateOf


class FakeRemoteRepository {
    private val fakeCurrencyAPI= mutableStateOf(FakeCurrencyAPI())
    val dataSource = RemoteDataSource(currencyAPI = fakeCurrencyAPI.value)
}