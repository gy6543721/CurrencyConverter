package levilin.currencyconverter.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import levilin.currencyconverter.data.remote.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import levilin.currencyconverter.data.local.FakeCurrencyItemDAO
import levilin.currencyconverter.data.local.LocalRepository
import levilin.currencyconverter.data.remote.FakeRemoteRepository
import levilin.currencyconverter.model.local.CurrencyItem
import levilin.currencyconverter.model.remote.CurrencyExchangeRate
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


// It is difficult to do unit test when we use dagger hilt to inject

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SharedViewModelTest {

    // Fake remote repository
    private val remoteDataSource = FakeRemoteRepository().dataSource
    private val fakeRemoteRepository = RemoteRepository(remoteDataSource = remoteDataSource)

    // Enable main thread coroutine ViewModel Launch test
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
    }

    @Test
    fun `Test fakeRemoteRepository getCurrencyCode()`() = runBlocking {
        val expected = 200
        val actual = fakeRemoteRepository.remoteDataSource.getCurrencyCode().code()
        assert(expected == actual)
    }

    @Test
    fun `Test fakeRemoteRepository getCurrencyExchangeRate`() = runBlocking {
        val query = HashMap<String, String>()
        query["Test"] = "Test"
        val expected = 200
        val actual = fakeRemoteRepository.remoteDataSource.getCurrencyExchangeRate(queries = query).code()
        assert(expected == actual)
    }
}