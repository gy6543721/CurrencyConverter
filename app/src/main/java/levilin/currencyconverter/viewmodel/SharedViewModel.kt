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
import levilin.currencyconverter.data.remote.Repository
import levilin.currencyconverter.model.CurrencyData
import levilin.currencyconverter.model.remote.CountryName
import levilin.currencyconverter.model.remote.CurrencyExchangeRate
import levilin.currencyconverter.model.remote.toCurrencyCode
import levilin.currencyconverter.model.remote.toOrder
import levilin.currencyconverter.utility.*
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: Repository, application: Application): AndroidViewModel(application) {
    var countryNameResponse: MutableLiveData<NetworkResult<CountryName>> = MutableLiveData()
    var currencyExchangeRateResponse: MutableLiveData<NetworkResult<CurrencyExchangeRate>> = MutableLiveData()

    // Local Data
    var countryNameList = CountryName()
    var currencyExchangeRateList = CurrencyExchangeRate()
    var currencyDataList: MutableLiveData<List<CurrencyData>> = MutableLiveData()

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

    fun getCurrencyDataList() {
        viewModelScope.launch {
            getCountryName()
            getCurrencyExchangeRate(queries = provideQueriesFree())
            currencyDataList = MutableLiveData(initCurrencyDataList(countryName = countryNameList, currencyExchangeRate = currencyExchangeRateList))
        }
        Log.d("TAG", "currencyDataList: ${currencyDataList.value?.get(0)?.countryName}")
    }

    fun getCountryName() {
        viewModelScope.launch {
            getCountryNameSafeCall()
            Log.d("TAG", "getCurrencyCode executed")
        }
    }

    fun getCurrencyExchangeRate(queries: Map<String, String>) {
        viewModelScope.launch {
            getCurrencyExchangeRateSafeCall(queries = queries)
            Log.d("TAG", "getCurrencyExchangeRate executed")
        }
    }

    private suspend fun getCountryNameSafeCall() {
        if (checkInternetConnection()) {
            try {
                val response = repository.dataSource.getCurrencyCode()
                Log.d("TAG", "getCurrencyCodeSafeCall Response: ${response.code()}")
                countryNameResponse.value = handleCountryNameResponse(response = response)

                countryNameList = initCountryName(countryName = countryNameResponse.value!!.data!!)
                Log.d("TAG", "countryNameList: ${countryNameList.aUD}")

            } catch (e: Exception) {
                countryNameResponse.value = NetworkResult.Error(message = repository.dataSource.getCurrencyCode().code().toString())
            }
        } else {
            countryNameResponse.value = NetworkResult.Error(message = "No Internet Connection")
        }

        Log.d("TAG", "CN Value: ${countryNameResponse.value?.data?.toString()}")
    }

    private suspend fun getCurrencyExchangeRateSafeCall(queries: Map<String, String>) {
        if (checkInternetConnection()) {
            try {
                val response = repository.dataSource.getCurrencyExchangeRate(queries = queries)
                Log.d("TAG", "getCurrencyExchangeRateSafeCall Response: ${response.code()}")
                currencyExchangeRateResponse.value = handleCurrencyExchangeRateResponse(response = response)

                currencyExchangeRateList = initCurrencyExchangeRate(currencyExchangeRate = currencyExchangeRateResponse.value!!.data!!)
                Log.d("TAG", "currencyExchangeRateList: ${currencyExchangeRateList.rates.aUD}")

            } catch (e: Exception) {
                currencyExchangeRateResponse.value = NetworkResult.Error(message = e.localizedMessage)
            }
        } else {
            currencyExchangeRateResponse.value = NetworkResult.Error(message = "No Internet Connection")
        }
    }

    private fun handleCountryNameResponse(response: Response<CountryName>): NetworkResult<CountryName> {
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

//    // Room DB
//    fun insertCurrencyData(context: Context, model: CurrencyData){
//        GlobalScope.launch {
//            getCurrencyDataDatabase(context = context).currencyDataDAO().insertItems(model)
//        }
//    }



    private fun initCountryName(countryName: CountryName): CountryName {
        return CountryName(
            aUD = countryName.aUD,
            bGN = countryName.bGN,
            bRL = countryName.bRL,
            cAD = countryName.cAD,
            cHF = countryName.cHF,
            cNY = countryName.cNY,
            cZK = countryName.cZK,
            dKK = countryName.dKK,
            eUR = countryName.eUR,
            gBP = countryName.gBP,
            hKD = countryName.hKD,
            hRK = countryName.hRK,
            hUF = countryName.hUF,
            iDR = countryName.iDR,
            iNR = countryName.iNR,
            iSK = countryName.iSK,
            jPY = countryName.jPY,
            kRW = countryName.kRW,
            mXN = countryName.mXN,
            mYR = countryName.mYR,
            nOK = countryName.nOK,
            nZD = countryName.nZD,
            pHP = countryName.pHP,
            pLN = countryName.pLN,
            rON = countryName.rON,
            rUB = countryName.rUB,
            sEK = countryName.sEK,
            sGD = countryName.sGD,
            tHB = countryName.tHB,
            tRY = countryName.tRY,
            tWD = countryName.tWD,
            uSD = countryName.uSD,
            zAR = countryName.zAR
        )
    }

    private fun initCurrencyExchangeRate(currencyExchangeRate: CurrencyExchangeRate): CurrencyExchangeRate {
        return CurrencyExchangeRate(
            rates = currencyExchangeRate.rates
        )
    }

    private fun initCurrencyDataList(countryName: CountryName, currencyExchangeRate: CurrencyExchangeRate): List<CurrencyData> {
        val resultList = ArrayList<CurrencyData>()
        val orderList = countryName.toOrder()
        val currencyCodeList = countryName.toCurrencyCode()

        resultList.add(CurrencyData(id = orderList.aUD.toInt(), countryName = countryName.aUD, currencyCode = currencyCodeList.aUD, currencyExchangeRate = currencyExchangeRate.rates.aUD, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.bGN.toInt(), countryName = countryName.bGN, currencyCode = currencyCodeList.bGN, currencyExchangeRate = currencyExchangeRate.rates.bGN, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.bRL.toInt(), countryName = countryName.bRL, currencyCode = currencyCodeList.bRL, currencyExchangeRate = currencyExchangeRate.rates.bRL, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.cAD.toInt(), countryName = countryName.cAD, currencyCode = currencyCodeList.cAD, currencyExchangeRate = currencyExchangeRate.rates.cAD, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.cHF.toInt(), countryName = countryName.cHF, currencyCode = currencyCodeList.cHF, currencyExchangeRate = currencyExchangeRate.rates.cHF, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.cNY.toInt(), countryName = countryName.cNY, currencyCode = currencyCodeList.cNY, currencyExchangeRate = currencyExchangeRate.rates.cNY, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.cZK.toInt(), countryName = countryName.cZK, currencyCode = currencyCodeList.cZK, currencyExchangeRate = currencyExchangeRate.rates.cZK, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.dKK.toInt(), countryName = countryName.dKK, currencyCode = currencyCodeList.dKK, currencyExchangeRate = currencyExchangeRate.rates.dKK, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.eUR.toInt(), countryName = countryName.eUR, currencyCode = currencyCodeList.eUR, currencyExchangeRate = currencyExchangeRate.rates.eUR, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.gBP.toInt(), countryName = countryName.gBP, currencyCode = currencyCodeList.gBP, currencyExchangeRate = currencyExchangeRate.rates.gBP, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.hKD.toInt(), countryName = countryName.hKD, currencyCode = currencyCodeList.hKD, currencyExchangeRate = currencyExchangeRate.rates.hKD, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.hRK.toInt(), countryName = countryName.hRK, currencyCode = currencyCodeList.hRK, currencyExchangeRate = currencyExchangeRate.rates.hRK, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.hUF.toInt(), countryName = countryName.hUF, currencyCode = currencyCodeList.hUF, currencyExchangeRate = currencyExchangeRate.rates.hUF, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.iDR.toInt(), countryName = countryName.iDR, currencyCode = currencyCodeList.iDR, currencyExchangeRate = currencyExchangeRate.rates.iDR, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.iNR.toInt(), countryName = countryName.iNR, currencyCode = currencyCodeList.iNR, currencyExchangeRate = currencyExchangeRate.rates.iNR, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.iSK.toInt(), countryName = countryName.iSK, currencyCode = currencyCodeList.iSK, currencyExchangeRate = currencyExchangeRate.rates.iSK, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.jPY.toInt(), countryName = countryName.jPY, currencyCode = currencyCodeList.jPY, currencyExchangeRate = currencyExchangeRate.rates.jPY, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.kRW.toInt(), countryName = countryName.kRW, currencyCode = currencyCodeList.kRW, currencyExchangeRate = currencyExchangeRate.rates.kRW, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.mXN.toInt(), countryName = countryName.mXN, currencyCode = currencyCodeList.mXN, currencyExchangeRate = currencyExchangeRate.rates.mXN, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.mYR.toInt(), countryName = countryName.mYR, currencyCode = currencyCodeList.mYR, currencyExchangeRate = currencyExchangeRate.rates.mYR, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.nOK.toInt(), countryName = countryName.nOK, currencyCode = currencyCodeList.nOK, currencyExchangeRate = currencyExchangeRate.rates.nOK, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.nZD.toInt(), countryName = countryName.nZD, currencyCode = currencyCodeList.nZD, currencyExchangeRate = currencyExchangeRate.rates.nZD, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.pHP.toInt(), countryName = countryName.pHP, currencyCode = currencyCodeList.pHP, currencyExchangeRate = currencyExchangeRate.rates.pHP, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.pLN.toInt(), countryName = countryName.pLN, currencyCode = currencyCodeList.pLN, currencyExchangeRate = currencyExchangeRate.rates.pLN, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.rON.toInt(), countryName = countryName.rON, currencyCode = currencyCodeList.rON, currencyExchangeRate = currencyExchangeRate.rates.rON, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.rUB.toInt(), countryName = countryName.rUB, currencyCode = currencyCodeList.rUB, currencyExchangeRate = currencyExchangeRate.rates.rUB, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.sEK.toInt(), countryName = countryName.sEK, currencyCode = currencyCodeList.sEK, currencyExchangeRate = currencyExchangeRate.rates.sEK, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.sGD.toInt(), countryName = countryName.sGD, currencyCode = currencyCodeList.sGD, currencyExchangeRate = currencyExchangeRate.rates.sGD, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.tHB.toInt(), countryName = countryName.tHB, currencyCode = currencyCodeList.tHB, currencyExchangeRate = currencyExchangeRate.rates.tHB, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.tRY.toInt(), countryName = countryName.tRY, currencyCode = currencyCodeList.tRY, currencyExchangeRate = currencyExchangeRate.rates.tRY, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.tWD.toInt(), countryName = countryName.tWD, currencyCode = currencyCodeList.tWD, currencyExchangeRate = currencyExchangeRate.rates.tWD, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.uSD.toInt(), countryName = countryName.uSD, currencyCode = currencyCodeList.uSD, currencyExchangeRate = currencyExchangeRate.rates.uSD, convertedValue = ""))
        resultList.add(CurrencyData(id = orderList.zAR.toInt(), countryName = countryName.zAR, currencyCode = currencyCodeList.zAR, currencyExchangeRate = currencyExchangeRate.rates.zAR, convertedValue = ""))

        return resultList
    }

    private fun provideQueriesFree(): Map<String, String> {
        val queries = HashMap<String, String>()
        queries.apply {
            this["app_id"] = ConstantValue.APP_ID
        }
        return queries
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