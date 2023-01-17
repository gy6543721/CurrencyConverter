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
                currencyCodeResponse.value = NetworkResult.Error(message = repository.dataSource.getCurrencyCode().code().toString())
            }
        } else {
            currencyCodeResponse.value = NetworkResult.Error(message = "No Internet Connection")
        }

        Log.d("TAG", "CCR Value: ${currencyCodeResponse.value?.data?.toString()}")
    }

    private suspend fun getCurrencyExchangeRateSafeCall(queries: Map<String, String>) {
        if (checkInternetConnection()) {
            try {
                val response = repository.dataSource.getCurrencyExchangeRate(queries = queries)
                Log.d("TAG", "getCurrencyExchangeRateSafeCall Response: ${response.code()}")
                currencyExchangeRateResponse.value = handleCurrencyExchangeRateResponse(response = response)
            } catch (e: Exception) {
                currencyExchangeRateResponse.value = NetworkResult.Error(message = e.localizedMessage)
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
                NetworkResult.Error(message = response.message().toString())
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
                NetworkResult.Error(message = response.message().toString())
            }
        }
    }

//    fun convertRatesToArray(rates: Rates): ArrayList<CurrencyExchangeRate> {
//
//        val list: ArrayList<CurrencyExchangeRate> = ArrayList(rates.values())
//
//        return when() {
//            rates.aUD -> 0
//            bGN -> 1
////        "BHD" -> rates.bHD
////        "BIF" -> rates.bIF
////        "BMD" -> rates.bMD
////        "BND" -> rates.bND
////        "BOB" -> rates.bOB
//            "BRL" -> rates.bRL
////        "BSD" -> rates.bSD
//            "BTC" -> rates.bTC
////        "BTN" -> rates.bTN
////        "BWP" -> rates.bWP
////        "BYN" -> rates.bYN
////        "BZD" -> rates.bZD
////        "CAD" -> rates.cAD
////        "CDF" -> rates.cDF
//            "CHF" -> rates.cHF
////        "CLF" -> rates.cLF
////        "CLP" -> rates.cLP
////        "CNH" -> rates.cNH
//            "CNY" -> rates.cNY
////        "COP" -> rates.cOP
////        "CRC" -> rates.cRC
////        "CUC" -> rates.cUC
////        "CUP" -> rates.cUP
////        "CVE" -> rates.cVE
//            "CZK" -> rates.cZK
////        "DJF" -> rates.dJF
//            "DKK" -> rates.dKK
////        "DOP" -> rates.dOP
////        "DZD" -> rates.dZD
////        "EGP" -> rates.eGP
////        "ERN" -> rates.eRN
////        "ETB" -> rates.eTB
//            "EUR" -> rates.eUR
////        "FJD" -> rates.fJD
////        "FKP" -> rates.fKP
//            "GBP" -> rates.gBP
////        "GEL" -> rates.gEL
////        "GGP" -> rates.gGP
////        "GHS" -> rates.gHS
////        "GIP" -> rates.gIP
////        "GMD" -> rates.gMD
////        "GNF" -> rates.gNF
////        "GTQ" -> rates.gTQ
////        "GYD" -> rates.gYD
//            "HKD" -> rates.hKD
////        "HNL" -> rates.hNL
//            "HRK" -> rates.hRK
////        "HTG" -> rates.hTG
//            "HUF" -> rates.hUF
//            "IDR" -> rates.iDR
////        "ILS" -> rates.iLS
////        "IMP" -> rates.iMP
//            "INR" -> rates.iNR
////        "IQD" -> rates.iQD
////        "IRR" -> rates.iRR
//            "ISK" -> rates.iSK
////        "JEP" -> rates.jEP
////        "JMD" -> rates.jMD
////        "JOD" -> rates.jOD
//            "JPY" -> rates.jPY
////        "KES" -> rates.kES
////        "KGS" -> rates.kGS
////        "KHR" -> rates.kHR
////        "KMF" -> rates.kMF
////        "KPW" -> rates.kPW
//            "KRW" -> rates.kRW
////        "KWD" -> rates.kWD
////        "KYD" -> rates.kYD
////        "KZT" -> rates.kZT
////        "LAK" -> rates.lAK
////        "LBP" -> rates.lBP
////        "LKR" -> rates.lKR
////        "LRD" -> rates.lRD
////        "LSL" -> rates.lSL
////        "LYD" -> rates.lYD
////        "MAD" -> rates.mAD
////        "MDL" -> rates.mDL
////        "MGA" -> rates.mGA
////        "MKD" -> rates.mKD
////        "MMK" -> rates.mMK
////        "MNT" -> rates.mNT
////        "MOP" -> rates.mOP
////        "MRU" -> rates.mRU
////        "MUR" -> rates.mUR
////        "MVR" -> rates.mVR
////        "MWK" -> rates.mWK
//            "MXN" -> rates.mXN
//            "MYR" -> rates.mYR
////        "MZN" -> rates.mZN
////        "NAD" -> rates.nAD
////        "NGN" -> rates.nGN
////        "NIO" -> rates.nIO
//            "NOK" -> rates.nOK
////        "NPR" -> rates.nPR
//            "NZD" -> rates.nZD
////        "OMR" -> rates.oMR
////        "PAB" -> rates.pAB
////        "PEN" -> rates.pEN
////        "PGK" -> rates.pGK
//            "PHP" -> rates.pHP
////        "PKR" -> rates.pKR
//            "PLN" -> rates.pLN
////        "PYG" -> rates.pYG
////        "QAR" -> rates.qAR
//            "RON" -> rates.rON
////        "RSD" -> rates.rSD
//            "RUB" -> rates.rUB
////        "RWF" -> rates.rWF
////        "SAR" -> rates.sAR
////        "SBD" -> rates.sBD
////        "SCR" -> rates.sCR
////        "SDG" -> rates.sDG
//            "SEK" -> rates.sEK
//            "SGD" -> rates.sGD
////        "SHP" -> rates.sHP
////        "SLL" -> rates.sLL
////        "SOS" -> rates.sOS
////        "SRD" -> rates.sRD
////        "SSP" -> rates.sSP
////        "STD" -> rates.sTD
////        "STN" -> rates.sTN
////        "SVC" -> rates.sVC
////        "SYP" -> rates.sYP
////        "SZL" -> rates.sZL
//            "THB" -> rates.tHB
////        "TJS" -> rates.tJS
////        "TMT" -> rates.tMT
////        "TND" -> rates.tND
////        "TOP" -> rates.tOP
//            "TRY" -> rates.tRY
////        "TTD" -> rates.tTD
//            "TWD" -> rates.tWD
////        "TZS" -> rates.tZS
////        "UAH" -> rates.uAH
////        "UGX" -> rates.uGX
//            "USD" -> rates.uSD
////        "UYU" -> rates.uYU
////        "UZS" -> rates.uZS
////        "VES" -> rates.vES
////        "VND" -> rates.vND
////        "VUV" -> rates.vUV
////        "WST" -> rates.wST
////        "XAF" -> rates.xAF
////        "XAG" -> rates.xAG
////        "XAU" -> rates.xAU
////        "XCD" -> rates.xCD
////        "XDR" -> rates.xDR
////        "XOF" -> rates.xOF
////        "XPD" -> rates.xPD
////        "XPF" -> rates.xPF
////        "XPT" -> rates.xPT
////        "YER" -> rates.yER
//            "ZAR" -> rates.zAR
////        "ZMW" -> rates.zMW
////        "ZWL" -> rates.zWL
//            else -> 0.00
//        }
//    }

}