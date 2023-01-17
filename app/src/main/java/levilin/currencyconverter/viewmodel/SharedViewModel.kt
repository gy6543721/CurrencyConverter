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
    var currencyExchangeRateResponseFree: MutableLiveData<NetworkResult<CurrencyExchangeRate>> = MutableLiveData()
    var currencyExchangeRateResponse: MutableLiveData<NetworkResult<CurrencyExchangeRate>> = MutableLiveData()

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
            Log.d("TAG", "getCurrencyCode executed")
        }
    }

    fun getCurrencyExchangeRateFree(app_id: String) {
        viewModelScope.launch {
            getCurrencyExchangeRateFreeSafeCall(app_id = app_id)
            Log.d("TAG", "getCurrencyExchangeRateFree executed")
        }
    }

    fun getCurrencyExchangeRate(queries: Map<String, String>) {
        viewModelScope.launch {
            getCurrencyExchangeRateSafeCall(queries = queries)
            Log.d("TAG", "getCurrencyExchangeRate executed")
        }
    }

    private suspend fun getCurrencyCodeSafeCall() {
        if (checkInternetConnection()) {
            try {
                val response = repository.dataSource.getCurrencyCode()
                Log.d("TAG", "getCurrencyCodeSafeCall Response: ${response.code()}")
                currencyCodeResponse.value = handleCurrencyCodeResponse(response = response)

            } catch (e: Exception) {
                currencyCodeResponse.value = NetworkResult.Error(message = "No Response")
            }
        } else {
            currencyCodeResponse.value = NetworkResult.Error(message = "No Internet Connection")
        }

        Log.d("TAG", "CCR Value: ${currencyCodeResponse.value?.data?.aED.toString()}")
    }

    private suspend fun getCurrencyExchangeRateFreeSafeCall(app_id: String) {
        if (checkInternetConnection()) {
            try {
                val response = repository.dataSource.getCurrencyExchangeRateFree(app_id = app_id)
                Log.d("TAG", "getCurrencyExchangeRateFreeSafeCall Response: ${response.code()}")
                currencyExchangeRateResponseFree.value = handleCurrencyExchangeRateFreeResponse(response = response)

            } catch (e: Exception) {
                currencyExchangeRateResponseFree.value = NetworkResult.Error(message = "No Response")
            }
        } else {
            currencyExchangeRateResponseFree.value = NetworkResult.Error(message = "No Internet Connection")
        }
    }


    private suspend fun getCurrencyExchangeRateSafeCall(queries: Map<String, String>) {
        if (checkInternetConnection()) {
            try {
                val response = repository.dataSource.getCurrencyExchangeRate(queries = queries)
                Log.d("TAG", "getCurrencyExchangeRateSafeCall Response: ${response.code()}")
                currencyExchangeRateResponse.value = handleCurrencyExchangeRateResponse(response = response)
            } catch (e: Exception) {
                currencyExchangeRateResponse.value = NetworkResult.Error(message = "No Response")
            }
        } else {
            currencyExchangeRateResponse.value = NetworkResult.Error(message = "No Internet Connection")
        }
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

    private fun handleCurrencyExchangeRateFreeResponse(response: Response<CurrencyExchangeRate>): NetworkResult<CurrencyExchangeRate> {
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

    private fun handleCurrencyExchangeRateResponse(response: Response<CurrencyExchangeRate>): NetworkResult<CurrencyExchangeRate> {
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