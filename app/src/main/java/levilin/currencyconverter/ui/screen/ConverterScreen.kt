package levilin.currencyconverter.ui.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.*
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.launch
import levilin.currencyconverter.R
import levilin.currencyconverter.model.CurrencyItem
import levilin.currencyconverter.model.Rates
import levilin.currencyconverter.ui.theme.*
import levilin.currencyconverter.utility.ConstantValue
import levilin.currencyconverter.utility.ConstantValue.Companion.CURRENCY_LIST
import levilin.currencyconverter.utility.NetworkResult
import levilin.currencyconverter.viewmodel.SharedViewModel

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun ConverterScreen(context: Context, sharedViewModel: SharedViewModel) {

    // Scaffold Control
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    // Focus Control
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val fromCurrencyCode: MutableState<String> = remember { mutableStateOf("USD") }
    val toCurrencyCode: MutableState<String> = remember { mutableStateOf("JPY") }
    var valueToConvert: MutableState<String> = remember { mutableStateOf("") }


    val convertedValue = remember { mutableStateOf("") }
    val singleConvertedValue = remember { mutableStateOf("") }

    var convertedValueList: ArrayList<String> = ArrayList(CURRENCY_LIST.size)
    var singleConvertedValueList: ArrayList<String> = ArrayList(CURRENCY_LIST.size)

//    sharedViewModel.getCurrencyCode()

    BottomSheetScaffold(
        sheetContent = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(CURRENCY_LIST) { item ->
                        Text(
                            text = "${item.countryName}\t (${item.currencyCode})",
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .clickable {
                                    fromCurrencyCode.value = item.currencyCode
                                    sharedViewModel.getCurrencyExchangeRate(
                                        queries = provideQueries(base = fromCurrencyCode.value)
                                    )
                                    sharedViewModel.currencyExchangeRateResponse.observe(context as LifecycleOwner) { networkResult ->
                                        when (networkResult) {
                                            is NetworkResult.Success -> {
                                                networkResult.data?.let { currencyData ->

                                                    Log.d(
                                                        "TAG",
                                                        "currencyExchangeRate: ${currencyData.rates}"
                                                    )

                                                    if (valueToConvert.value.isEmpty()) {
                                                        valueToConvert.value = "1.00"
                                                    }
                                                    val valueToConvertDouble =
                                                        valueToConvert.value.toDouble()

                                                    val fromCurrencyValue =
                                                        convertRatesByCurrencyCode(
                                                            fromCurrencyCode.value,
                                                            currencyData.rates
                                                        )

                                                    val toCurrencyValue =
                                                        convertRatesByCurrencyCode(
                                                            toCurrencyCode.value,
                                                            currencyData.rates
                                                        )

                                                    convertedValue.value =
                                                        "${formattedResult(valueToConvertDouble * toCurrencyValue / fromCurrencyValue)} ${toCurrencyCode.value}"

                                                    singleConvertedValue.value =
                                                        "1 ${fromCurrencyCode.value} = ${
                                                            formattedResult(
                                                                toCurrencyValue / fromCurrencyValue
                                                            )
                                                        } ${toCurrencyCode.value}"

                                                    Log.d(
                                                        "TAG",
                                                        "convertedValue: ${convertedValue.value}"
                                                    )
                                                    Log.d(
                                                        "TAG",
                                                        "singleConvertedValue: ${singleConvertedValue.value}"
                                                    )
                                                }
                                            }
                                            is NetworkResult.Error -> {
                                                Toast
                                                    .makeText(
                                                        context,
                                                        networkResult.message,
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                                Log.d("TAG", "Error: ${networkResult.message}")
                                            }
                                            is NetworkResult.Loading -> {
                                                Toast
                                                    .makeText(
                                                        context,
                                                        "Loading...",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            }
                                        }
                                    }
                                    scope.launch {
                                        scaffoldState.bottomSheetState.collapse()
                                    }
                                }
                        )
                    }
                }
            }
        },
        topBar = { TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 24.sp,
                    color = MaterialTheme.colors.converterScreenTextColor,
                    style = TextStyle(
                        fontWeight = FontWeight.Black,
                        fontSize = 30.sp
                    )
                )
            },
            backgroundColor = MaterialTheme.colors.converterScreenBackgroundColor,
            elevation = 0.dp
        ) },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetElevation = 6.dp,
        sheetBackgroundColor = MaterialTheme.colors.converterScreenBackgroundColor,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
    ) {
        // Value Input
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Amount", color = MaterialTheme.colors.converterScreenTextColor)
                Text(text = fromCurrencyCode.value, color = MaterialTheme.colors.converterScreenTextColor)
            }
            Spacer(modifier = Modifier.padding(3.dp))
            OutlinedTextField(
                value = valueToConvert.value,
                onValueChange = { valueToConvert.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = {
                    Text(text = valueToConvert.value, style = MaterialTheme.typography.body1)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { TextFieldValue("") }) {
                        Icon(modifier = Modifier.alpha(ContentAlpha.medium), imageVector = Icons.Filled.Close, contentDescription = stringResource(id = R.string.trail_icon_text), tint = MaterialTheme.colors.boxIconColor)
                    }
                }
            )
        }

        // Selection List
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ) {
            Text(text = "From", color = MaterialTheme.colors.converterScreenTextColor)
            Spacer(modifier = Modifier.padding(3.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        focusManager.clearFocus()
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.boxBorderColor,
                        shape = RoundedCornerShape(5.dp)
                    )
            ) {
                Text(text = fromCurrencyCode.value, modifier = Modifier.padding(10.dp), color = MaterialTheme.colors.converterScreenTextColor)
            }
        }

        val list = createDataList()

        // Grid View
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                cells = GridCells.Adaptive(100.dp),
                content = {
                    items(list.size) { item ->

                        GridItem(currencyItem = list[item], onItemClicked = {
                            Toast.makeText(context, it.currencyCode, Toast.LENGTH_SHORT).show()
                        })




//                        Box(
//                            modifier = Modifier
//                                .padding(8.dp)
//                                .aspectRatio(1f)
//                                .clip(RoundedCornerShape(5.dp))
//                                .background(MaterialTheme.colors.boxBackgroundColor),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            sharedViewModel.currencyExchangeRateResponse.observe(context as LifecycleOwner) { networkResult ->
//                                when (networkResult) {
//                                    is NetworkResult.Success -> {
//                                        networkResult.data?.let { currencyData ->
//
//                                            Log.d(
//                                                "TAG",
//                                                "currencyExchangeRate: ${currencyData.rates}"
//                                            )
//
//                                            if (valueToConvert.value.isEmpty()) {
//                                                valueToConvert.value = "1.00"
//                                            }
//                                            val valueToConvertDouble =
//                                                valueToConvert.value.toDouble()
//
//                                            val fromCurrencyValue =
//                                                convertRatesByCurrencyCode(
//                                                    fromCurrencyCode.value,
//                                                    currencyData.rates
//                                                )
//
//                                            val toCurrencyValue = convertRatesByCurrencyCode(
//                                                convertCurrencyCodeByOrder(item),
//                                                currencyData.rates
//                                            )
//
//                                            convertedValueList[item] =
//                                                "${formattedResult(valueToConvertDouble * toCurrencyValue / fromCurrencyValue)} ${toCurrencyCode.value}"
//
//                                            singleConvertedValueList[item] =
//                                                "1 ${fromCurrencyCode.value} = ${formattedResult(toCurrencyValue / fromCurrencyValue)} ${toCurrencyCode.value}"
//
//                                            Log.d("TAG", "convertedValue ${item}: ${convertedValue.value}")
//                                            Log.d("TAG", "singleConvertedValue ${item}: ${singleConvertedValue.value}")
//
//                                        }
//                                    }
//                                    is NetworkResult.Error -> {
//                                        Toast
//                                            .makeText(
//                                                context,
//                                                networkResult.message,
//                                                Toast.LENGTH_SHORT
//                                            )
//                                            .show()
//                                        Log.d("TAG", "Error: ${networkResult.message}")
//                                    }
//                                    is NetworkResult.Loading -> {
//                                        Toast
//                                            .makeText(
//                                                context,
//                                                "Loading...",
//                                                Toast.LENGTH_SHORT
//                                            )
//                                            .show()
//                                    }
//                                }
//                            }

//                            val list = createDataList()
//                            items(list.size) { index ->
//                                GridItem(currencyItem = list[index], onItemClicked = {
//                                    Toast.makeText(context, it.countryName, Toast.LENGTH_SHORT).show()
//                                })
//                            }
//                        }


                    }
                }
            )
        }
    }
}

@Composable
private fun GridItem(currencyItem: CurrencyItem, onItemClicked:(currencyItem: CurrencyItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .padding(8.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colors.boxBackgroundColor)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple(
                    bounded = true,
                    color = MaterialTheme.colors.boxBackgroundColor
                ),
                onClick = { onItemClicked(currencyItem) }
            ),
        backgroundColor = MaterialTheme.colors.boxBackgroundColor,
        elevation = 12.dp,
        shape = RoundedCornerShape(6.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = currencyItem.currencyCode)
            Text(text = currencyItem.countryName)
        }
    }
}

private fun createDataList(): List<CurrencyItem> {
    val list = mutableListOf<CurrencyItem>()

    list.add(CurrencyItem("Australia", "AUD"))
    list.add(CurrencyItem("BitCoin", "BTC"))
    list.add(CurrencyItem("Brazil", "BRL"))
    list.add(CurrencyItem("Bulgaria", "BGN"))

    return list
}


fun formattedResult(value: Double): String {
    return "%.2f".format(value)
}

fun convertOrderByCurrencyCode(currencyCode: String): Int {
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

fun convertCurrencyCodeByOrder(order: Int): String {
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

fun convertRatesByCurrencyCode(currencyCode: String, rates: Rates): Double {
    return when(currencyCode) {
//        "AED" -> rates.aED
//        "AFN" -> rates.aFN
//        "ALL" -> rates.aLL
//        "AMD" -> rates.aMD
//        "ANG" -> rates.aNG
//        "AOA" -> rates.aOA
//        "ARS" -> rates.aRS
        "AUD" -> rates.aUD
//        "AWG" -> rates.aWG
//        "AZN" -> rates.aZN
//        "BAM" -> rates.bAM
//        "BBD" -> rates.bBD
//        "BDT" -> rates.bDT
        "BGN" -> rates.bGN
//        "BHD" -> rates.bHD
//        "BIF" -> rates.bIF
//        "BMD" -> rates.bMD
//        "BND" -> rates.bND
//        "BOB" -> rates.bOB
        "BRL" -> rates.bRL
//        "BSD" -> rates.bSD
//        "BTC" -> rates.bTC
//        "BTN" -> rates.bTN
//        "BWP" -> rates.bWP
//        "BYN" -> rates.bYN
//        "BZD" -> rates.bZD
        "CAD" -> rates.cAD
//        "CDF" -> rates.cDF
        "CHF" -> rates.cHF
//        "CLF" -> rates.cLF
//        "CLP" -> rates.cLP
//        "CNH" -> rates.cNH
        "CNY" -> rates.cNY
//        "COP" -> rates.cOP
//        "CRC" -> rates.cRC
//        "CUC" -> rates.cUC
//        "CUP" -> rates.cUP
//        "CVE" -> rates.cVE
        "CZK" -> rates.cZK
//        "DJF" -> rates.dJF
        "DKK" -> rates.dKK
//        "DOP" -> rates.dOP
//        "DZD" -> rates.dZD
//        "EGP" -> rates.eGP
//        "ERN" -> rates.eRN
//        "ETB" -> rates.eTB
        "EUR" -> rates.eUR
//        "FJD" -> rates.fJD
//        "FKP" -> rates.fKP
        "GBP" -> rates.gBP
//        "GEL" -> rates.gEL
//        "GGP" -> rates.gGP
//        "GHS" -> rates.gHS
//        "GIP" -> rates.gIP
//        "GMD" -> rates.gMD
//        "GNF" -> rates.gNF
//        "GTQ" -> rates.gTQ
//        "GYD" -> rates.gYD
        "HKD" -> rates.hKD
//        "HNL" -> rates.hNL
        "HRK" -> rates.hRK
//        "HTG" -> rates.hTG
        "HUF" -> rates.hUF
        "IDR" -> rates.iDR
//        "ILS" -> rates.iLS
//        "IMP" -> rates.iMP
        "INR" -> rates.iNR
//        "IQD" -> rates.iQD
//        "IRR" -> rates.iRR
        "ISK" -> rates.iSK
//        "JEP" -> rates.jEP
//        "JMD" -> rates.jMD
//        "JOD" -> rates.jOD
        "JPY" -> rates.jPY
//        "KES" -> rates.kES
//        "KGS" -> rates.kGS
//        "KHR" -> rates.kHR
//        "KMF" -> rates.kMF
//        "KPW" -> rates.kPW
        "KRW" -> rates.kRW
//        "KWD" -> rates.kWD
//        "KYD" -> rates.kYD
//        "KZT" -> rates.kZT
//        "LAK" -> rates.lAK
//        "LBP" -> rates.lBP
//        "LKR" -> rates.lKR
//        "LRD" -> rates.lRD
//        "LSL" -> rates.lSL
//        "LYD" -> rates.lYD
//        "MAD" -> rates.mAD
//        "MDL" -> rates.mDL
//        "MGA" -> rates.mGA
//        "MKD" -> rates.mKD
//        "MMK" -> rates.mMK
//        "MNT" -> rates.mNT
//        "MOP" -> rates.mOP
//        "MRU" -> rates.mRU
//        "MUR" -> rates.mUR
//        "MVR" -> rates.mVR
//        "MWK" -> rates.mWK
        "MXN" -> rates.mXN
        "MYR" -> rates.mYR
//        "MZN" -> rates.mZN
//        "NAD" -> rates.nAD
//        "NGN" -> rates.nGN
//        "NIO" -> rates.nIO
        "NOK" -> rates.nOK
//        "NPR" -> rates.nPR
        "NZD" -> rates.nZD
//        "OMR" -> rates.oMR
//        "PAB" -> rates.pAB
//        "PEN" -> rates.pEN
//        "PGK" -> rates.pGK
        "PHP" -> rates.pHP
//        "PKR" -> rates.pKR
        "PLN" -> rates.pLN
//        "PYG" -> rates.pYG
//        "QAR" -> rates.qAR
        "RON" -> rates.rON
//        "RSD" -> rates.rSD
        "RUB" -> rates.rUB
//        "RWF" -> rates.rWF
//        "SAR" -> rates.sAR
//        "SBD" -> rates.sBD
//        "SCR" -> rates.sCR
//        "SDG" -> rates.sDG
        "SEK" -> rates.sEK
        "SGD" -> rates.sGD
//        "SHP" -> rates.sHP
//        "SLL" -> rates.sLL
//        "SOS" -> rates.sOS
//        "SRD" -> rates.sRD
//        "SSP" -> rates.sSP
//        "STD" -> rates.sTD
//        "STN" -> rates.sTN
//        "SVC" -> rates.sVC
//        "SYP" -> rates.sYP
//        "SZL" -> rates.sZL
        "THB" -> rates.tHB
//        "TJS" -> rates.tJS
//        "TMT" -> rates.tMT
//        "TND" -> rates.tND
//        "TOP" -> rates.tOP
        "TRY" -> rates.tRY
//        "TTD" -> rates.tTD
        "TWD" -> rates.tWD
//        "TZS" -> rates.tZS
//        "UAH" -> rates.uAH
//        "UGX" -> rates.uGX
        "USD" -> rates.uSD
//        "UYU" -> rates.uYU
//        "UZS" -> rates.uZS
//        "VES" -> rates.vES
//        "VND" -> rates.vND
//        "VUV" -> rates.vUV
//        "WST" -> rates.wST
//        "XAF" -> rates.xAF
//        "XAG" -> rates.xAG
//        "XAU" -> rates.xAU
//        "XCD" -> rates.xCD
//        "XDR" -> rates.xDR
//        "XOF" -> rates.xOF
//        "XPD" -> rates.xPD
//        "XPF" -> rates.xPF
//        "XPT" -> rates.xPT
//        "YER" -> rates.yER
        "ZAR" -> rates.zAR
//        "ZMW" -> rates.zMW
//        "ZWL" -> rates.zWL
        else -> 1.00
    }
}

fun provideQueries(base: String): Map<String, String> {
    val queries = HashMap<String, String>()
    queries.apply {
        this["app_id"] = ConstantValue.APP_ID

        // only "USD" is provided in free plan, can set [base] in paid plan.
//        this["base"] = "USD"
    }
    return queries
}

//@ExperimentalMaterialApi
//@ExperimentalFoundationApi
//@Composable
//@Preview
//private fun ConverterScreenPreview() {
//    ConverterScreen()
//}