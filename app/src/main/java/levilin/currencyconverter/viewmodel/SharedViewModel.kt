package levilin.currencyconverter.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import levilin.currencyconverter.data.remote.Repository
import levilin.currencyconverter.model.CurrencyItem
import levilin.currencyconverter.model.remote.*
import levilin.currencyconverter.utility.*
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: Repository, application: Application, private val state: SavedStateHandle): AndroidViewModel(application) {
    var countryNameResponse: MutableLiveData<NetworkResult<CountryName>> = MutableLiveData()
    var currencyExchangeRateResponse: MutableLiveData<NetworkResult<CurrencyExchangeRate>> = MutableLiveData()

    var fromCurrencyCode: MutableState<String> = mutableStateOf("USD")
    var valueToConvert: MutableState<String> = mutableStateOf("1.00")
    private var convertedValue = mutableStateOf("")
    private var singleConvertedValue = mutableStateOf("")

    // Local Data
    private var countryNameList = CountryName()
    private var currencyExchangeRateList = CurrencyExchangeRate()
    var currencyItemList: MutableLiveData<List<CurrencyItem>> = MutableLiveData()

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

    fun getExchangeRateResult(currencyExchangeRate: CurrencyExchangeRate) {
        viewModelScope.launch {

            if (valueToConvert.value.isEmpty() || valueToConvert.value == "") {
                valueToConvert.value = "1.00"
            }

            val valueToConvertDouble =
                valueToConvert.value.toDouble()

            val fromCurrencyValue =
                convertRatesByCurrencyCode(
                    fromCurrencyCode.value,
                    currencyExchangeRate.rates
                )

            // convert each currency
            for (i in currencyItemList.value!!.indices) {

                val toCurrencyValue =
                    convertRatesByCurrencyCode(
                        convertCurrencyCodeByOrder(i),
                        currencyExchangeRate.rates
                    )

                convertedValue.value =
                    "${valueToConvert.value} ${fromCurrencyCode.value} = ${
                        formattedResult(
                            valueToConvertDouble * toCurrencyValue / fromCurrencyValue
                        )
                    } ${convertCurrencyCodeByOrder(i)}"
//                Log.d(
//                    "TAG",
//                    "SharedViewModel ConvertedValue: ${convertedValue.value}"
//                )

                singleConvertedValue.value =
                    "1 ${convertCurrencyCodeByOrder(i)} = ${
                        formattedResult(
                            fromCurrencyValue / toCurrencyValue
                        )
                    } ${fromCurrencyCode.value}"
//                Log.d(
//                    "TAG",
//                    "SharedViewModel SingleConvertedValue: ${singleConvertedValue.value}"
//                )

                currencyItemList.value!![i].convertedValue = convertedValue.value
                currencyItemList.value!![i].singleConvertedValue = singleConvertedValue.value
//                Log.d("TAG","SharedViewmodel ConvertedDataList: ${currencyItemList.value!![i].convertedValue}")
            }
        }
    }

    fun getCurrencyDataList() {
        viewModelScope.launch {
            getCountryName()
            getCurrencyExchangeRate(queries = provideQueriesFree())
            currencyItemList = MutableLiveData(initCurrencyItemList(countryName = countryNameList, currencyExchangeRate = currencyExchangeRateList))
        }
        Log.d("TAG", "currencyItemList: ${currencyItemList.value?.get(0)?.countryName}")
    }

    private fun getCountryName() {
        viewModelScope.launch {
            getCountryNameSafeCall()
            Log.d("TAG", "getCurrencyCode executed")
        }
    }

    private fun getCurrencyExchangeRate(queries: Map<String, String>) {
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

                for (i in currencyItemList.value!!.indices) {
                    currencyItemList.value!![i].countryName = convertCountryNameByOrder(order = i, countryName = countryNameResponse.value!!.data!!)
                }

                Log.d("TAG", "countryNameList: ${currencyItemList.value!![0].countryName}")

                countryNameList = initCountryName(countryName = countryNameResponse.value!!.data!!)
//                Log.d("TAG", "countryNameList: ${countryNameList.aUD}")

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

//                for (i in currencyItemList.value!!.indices) {
//                    currencyItemList.value!![i].currencyExchangeRate =
//                }
//
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
//    fun insertCurrencyData(context: Context, model: CurrencyItem){
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

    private fun initCurrencyItemList(countryName: CountryName, currencyExchangeRate: CurrencyExchangeRate): List<CurrencyItem> {
        val resultList = ArrayList<CurrencyItem>()
        val orderList = countryName.toOrder()
        val currencyCodeList = countryName.toCurrencyCode()

        resultList.add(CurrencyItem(id = orderList.aUD.toInt(), countryName = countryName.aUD, currencyCode = currencyCodeList.aUD, currencyExchangeRate = currencyExchangeRate.rates.aUD, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.bGN.toInt(), countryName = countryName.bGN, currencyCode = currencyCodeList.bGN, currencyExchangeRate = currencyExchangeRate.rates.bGN, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.bRL.toInt(), countryName = countryName.bRL, currencyCode = currencyCodeList.bRL, currencyExchangeRate = currencyExchangeRate.rates.bRL, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.cAD.toInt(), countryName = countryName.cAD, currencyCode = currencyCodeList.cAD, currencyExchangeRate = currencyExchangeRate.rates.cAD, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.cHF.toInt(), countryName = countryName.cHF, currencyCode = currencyCodeList.cHF, currencyExchangeRate = currencyExchangeRate.rates.cHF, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.cNY.toInt(), countryName = countryName.cNY, currencyCode = currencyCodeList.cNY, currencyExchangeRate = currencyExchangeRate.rates.cNY, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.cZK.toInt(), countryName = countryName.cZK, currencyCode = currencyCodeList.cZK, currencyExchangeRate = currencyExchangeRate.rates.cZK, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.dKK.toInt(), countryName = countryName.dKK, currencyCode = currencyCodeList.dKK, currencyExchangeRate = currencyExchangeRate.rates.dKK, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.eUR.toInt(), countryName = countryName.eUR, currencyCode = currencyCodeList.eUR, currencyExchangeRate = currencyExchangeRate.rates.eUR, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.gBP.toInt(), countryName = countryName.gBP, currencyCode = currencyCodeList.gBP, currencyExchangeRate = currencyExchangeRate.rates.gBP, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.hKD.toInt(), countryName = countryName.hKD, currencyCode = currencyCodeList.hKD, currencyExchangeRate = currencyExchangeRate.rates.hKD, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.hRK.toInt(), countryName = countryName.hRK, currencyCode = currencyCodeList.hRK, currencyExchangeRate = currencyExchangeRate.rates.hRK, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.hUF.toInt(), countryName = countryName.hUF, currencyCode = currencyCodeList.hUF, currencyExchangeRate = currencyExchangeRate.rates.hUF, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.iDR.toInt(), countryName = countryName.iDR, currencyCode = currencyCodeList.iDR, currencyExchangeRate = currencyExchangeRate.rates.iDR, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.iNR.toInt(), countryName = countryName.iNR, currencyCode = currencyCodeList.iNR, currencyExchangeRate = currencyExchangeRate.rates.iNR, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.iSK.toInt(), countryName = countryName.iSK, currencyCode = currencyCodeList.iSK, currencyExchangeRate = currencyExchangeRate.rates.iSK, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.jPY.toInt(), countryName = countryName.jPY, currencyCode = currencyCodeList.jPY, currencyExchangeRate = currencyExchangeRate.rates.jPY, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.kRW.toInt(), countryName = countryName.kRW, currencyCode = currencyCodeList.kRW, currencyExchangeRate = currencyExchangeRate.rates.kRW, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.mXN.toInt(), countryName = countryName.mXN, currencyCode = currencyCodeList.mXN, currencyExchangeRate = currencyExchangeRate.rates.mXN, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.mYR.toInt(), countryName = countryName.mYR, currencyCode = currencyCodeList.mYR, currencyExchangeRate = currencyExchangeRate.rates.mYR, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.nOK.toInt(), countryName = countryName.nOK, currencyCode = currencyCodeList.nOK, currencyExchangeRate = currencyExchangeRate.rates.nOK, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.nZD.toInt(), countryName = countryName.nZD, currencyCode = currencyCodeList.nZD, currencyExchangeRate = currencyExchangeRate.rates.nZD, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.pHP.toInt(), countryName = countryName.pHP, currencyCode = currencyCodeList.pHP, currencyExchangeRate = currencyExchangeRate.rates.pHP, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.pLN.toInt(), countryName = countryName.pLN, currencyCode = currencyCodeList.pLN, currencyExchangeRate = currencyExchangeRate.rates.pLN, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.rON.toInt(), countryName = countryName.rON, currencyCode = currencyCodeList.rON, currencyExchangeRate = currencyExchangeRate.rates.rON, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.rUB.toInt(), countryName = countryName.rUB, currencyCode = currencyCodeList.rUB, currencyExchangeRate = currencyExchangeRate.rates.rUB, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.sEK.toInt(), countryName = countryName.sEK, currencyCode = currencyCodeList.sEK, currencyExchangeRate = currencyExchangeRate.rates.sEK, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.sGD.toInt(), countryName = countryName.sGD, currencyCode = currencyCodeList.sGD, currencyExchangeRate = currencyExchangeRate.rates.sGD, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.tHB.toInt(), countryName = countryName.tHB, currencyCode = currencyCodeList.tHB, currencyExchangeRate = currencyExchangeRate.rates.tHB, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.tRY.toInt(), countryName = countryName.tRY, currencyCode = currencyCodeList.tRY, currencyExchangeRate = currencyExchangeRate.rates.tRY, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.tWD.toInt(), countryName = countryName.tWD, currencyCode = currencyCodeList.tWD, currencyExchangeRate = currencyExchangeRate.rates.tWD, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.uSD.toInt(), countryName = countryName.uSD, currencyCode = currencyCodeList.uSD, currencyExchangeRate = currencyExchangeRate.rates.uSD, convertedValue = "", singleConvertedValue = ""))
        resultList.add(CurrencyItem(id = orderList.zAR.toInt(), countryName = countryName.zAR, currencyCode = currencyCodeList.zAR, currencyExchangeRate = currencyExchangeRate.rates.zAR, convertedValue = "", singleConvertedValue = ""))

        return resultList
    }

    private fun provideQueriesFree(): Map<String, String> {
        val queries = HashMap<String, String>()
        queries.apply {
            this["app_id"] = ConstantValue.APP_ID
        }
        return queries
    }

    private fun formattedResult(value: Double): String {
        if ("%.2f".format(value) == "0.00") {
            if ("%.4f".format(value) == "0.0000") {
                if ("%.6f".format(value) == "0.000000") {
                    return "%.8f".format(value)
                }
                return "%.6f".format(value)
            }
            return "%.4f".format(value)
        } else {
            return "%.2f".format(value)
        }
    }

    private fun convertCountryNameByOrder(order: Int, countryName: CountryName): String {
        return when(order) {
            0 -> countryName.aUD
            1 -> countryName.bGN
            2 -> countryName.bRL
            3 -> countryName.cAD
            4 -> countryName.cHF
            5 -> countryName.cNY
            6 -> countryName.cZK
            7 -> countryName.dKK
            8 -> countryName.eUR
            9 -> countryName.gBP
            10 -> countryName.hKD
            11 -> countryName.hRK
            12 -> countryName.hUF
            13 -> countryName.iDR
            14 -> countryName.iNR
            15 -> countryName.iSK
            16 -> countryName.jPY
            17 -> countryName.kRW
            18 -> countryName.mXN
            19 -> countryName.mYR
            20 -> countryName.nOK
            21 -> countryName.nZD
            22 -> countryName.pHP
            23 -> countryName.pLN
            24 -> countryName.rON
            25 -> countryName.rUB
            26 -> countryName.sEK
            27 -> countryName.sGD
            28 -> countryName.tHB
            29 -> countryName.tRY
            30 -> countryName.tWD
            31 -> countryName.uSD
            32 -> countryName.zAR
            else -> countryName.uSD
        }
    }

    private fun convertCurrencyCodeByOrder(order: Int): String {
        return when(order) {
            0 -> "AUD"
            1 -> "BGN"
            2 -> "BRL"
            3 -> "CAD"
            4 -> "CHF"
            5 -> "CNY"
            6 -> "CZK"
            7 -> "DKK"
            8 -> "EUR"
            9 -> "GBP"
            10 -> "HKD"
            11 -> "HRK"
            12 -> "HUF"
            13 -> "IDR"
            14 -> "INR"
            15 -> "ISK"
            16 -> "JPY"
            17 -> "KRW"
            18 -> "MXN"
            19 -> "MYR"
            20 -> "NOK"
            21 -> "NZD"
            22 -> "PHP"
            23 -> "PLN"
            24 -> "RON"
            25 -> "RUB"
            26 -> "SEK"
            27 -> "SGD"
            28 -> "THB"
            29 -> "TRY"
            30 -> "TWD"
            31 -> "USD"
            32 -> "ZAR"
            else -> "USD"
        }
    }

    private fun convertRatesByCurrencyCode(currencyCode: String, rates: Rates): Double {
        return when(currencyCode) {
            "AUD" -> rates.aUD
            "BGN" -> rates.bGN
            "BRL" -> rates.bRL
            "CAD" -> rates.cAD
            "CHF" -> rates.cHF
            "CNY" -> rates.cNY
            "CZK" -> rates.cZK
            "DKK" -> rates.dKK
            "EUR" -> rates.eUR
            "GBP" -> rates.gBP
            "HKD" -> rates.hKD
            "HRK" -> rates.hRK
            "HUF" -> rates.hUF
            "IDR" -> rates.iDR
            "INR" -> rates.iNR
            "ISK" -> rates.iSK
            "JPY" -> rates.jPY
            "KRW" -> rates.kRW
            "MXN" -> rates.mXN
            "MYR" -> rates.mYR
            "NOK" -> rates.nOK
            "NZD" -> rates.nZD
            "PHP" -> rates.pHP
            "PLN" -> rates.pLN
            "RON" -> rates.rON
            "RUB" -> rates.rUB
            "SEK" -> rates.sEK
            "SGD" -> rates.sGD
            "THB" -> rates.tHB
            "TRY" -> rates.tRY
            "TWD" -> rates.tWD
            "USD" -> rates.uSD
            "ZAR" -> rates.zAR
            else -> 1.00
        }
    }

    private fun convertOrderByCurrencyCode(currencyCode: String): Int {
        return when(currencyCode) {
            "AUD" -> 0
            "BGN" -> 1
            "BRL" -> 2
            "CAD" -> 3
            "CHF" -> 4
            "CNY" -> 5
            "CZK" -> 6
            "DKK" -> 7
            "EUR" -> 8
            "GBP" -> 9
            "HKD" -> 10
            "HRK" -> 11
            "HUF" -> 12
            "IDR" -> 13
            "INR" -> 14
            "ISK" -> 15
            "JPY" -> 16
            "KRW" -> 17
            "MXN" -> 18
            "MYR" -> 19
            "NOK" -> 20
            "NZD" -> 21
            "PHP" -> 22
            "PLN" -> 23
            "RON" -> 24
            "RUB" -> 25
            "SEK" -> 26
            "SGD" -> 27
            "THB" -> 28
            "TRY" -> 29
            "TWD" -> 30
            "USD" -> 31
            "ZAR" -> 32
            else -> 33
        }
    }
}