package levilin.currencyconverter.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import levilin.currencyconverter.data.Repository
import levilin.currencyconverter.model.*
import levilin.currencyconverter.utility.*
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: Repository, application: Application): AndroidViewModel(application) {
    var currencyCodeResponse: MutableLiveData<NetworkResult<CurrencyCode>> = MutableLiveData()
    var currencyExchangeRateResponse: MutableLiveData<NetworkResult<CurrencyData>> = MutableLiveData()

    var currencyItemList: List<CurrencyCode> = ArrayList()

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapability = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun getCurrencyCode() {
        viewModelScope.launch {
            getCurrencyCodeSafeCall()
        }
    }

    fun getCurrencyExchangeRate(queries: Map<String, String>) {
        viewModelScope.launch {
            getCurrencyExchangeRateSafeCall(queries = queries)
        }
    }

    private suspend fun getCurrencyCodeSafeCall() {
        if (checkInternetConnection()) {
            try {
                val response = repository.dataSource.getCurrencyCode()
                currencyCodeResponse.value = handleCurrencyCodeResponse(response = response)

            } catch (e: Exception) {
                currencyCodeResponse.value = NetworkResult.Error(message = "No Response.")
            }
        } else {
            currencyCodeResponse.value = NetworkResult.Error(message = "No Internet Connection")
        }

        Log.d("TAG", "CCR Value: ${currencyCodeResponse.value?.data?.aED.toString()}")
    }

    private suspend fun getCurrencyExchangeRateSafeCall(queries: Map<String, String>) {
        if (checkInternetConnection()) {
            try {
                val response = repository.dataSource.getCurrencyExchangeRate(queries = queries)
                currencyExchangeRateResponse.value = handleCurrencyExchangeRateResponse(response = response)
            } catch (e: Exception) {
                currencyExchangeRateResponse.value = NetworkResult.Error(message = "No Response.")
                Log.d("TAG", "isFailed: ${currencyExchangeRateResponse.value}")
            }
        } else {
            currencyExchangeRateResponse.value = NetworkResult.Error(message = "No Internet Connection")
        }

        Log.d("TAG", "CERR Value: ${currencyExchangeRateResponse.value?.data?.rates.toString()}")
    }

    private fun handleCurrencyCodeResponse(response: Response<CurrencyCode>): NetworkResult<CurrencyCode> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error(message = "Time Out")
            }
            response.isSuccessful -> {
                val currencyCodeResponse = response.body()
                NetworkResult.Success(data = currencyCodeResponse!!)
            }
            else -> {
                NetworkResult.Error(message = "Cannot Fetch Data Successfully")
            }
        }
    }

    private fun handleCurrencyExchangeRateResponse(response: Response<CurrencyData>): NetworkResult<CurrencyData> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error(message = "Time Out")
            }
            response.isSuccessful -> {
                val currencyExchangeRayeResponse = response.body()
                NetworkResult.Success(data = currencyExchangeRayeResponse!!)
            }
            else -> {
                NetworkResult.Error(message = "Cannot Fetch Data Successfully")
            }
        }
    }
}