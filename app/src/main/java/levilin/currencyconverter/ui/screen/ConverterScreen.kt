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
import levilin.currencyconverter.model.*
import levilin.currencyconverter.model.remote.*
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
    val valueToConvert: MutableState<String> = remember { mutableStateOf("1.00") }

    val convertedValue = remember { mutableStateOf("") }
    val singleConvertedValue = remember { mutableStateOf("") }

    // Local Data
    var countryNameList = CountryName()
    var currencyExchangeRateList = CurrencyExchangeRate()

    sharedViewModel.getCurrencyDataList()
    Log.d("TAG","sharedviewmodel: ${sharedViewModel.currencyDataList.value!![0].countryName}")

    // initialize local data
    sharedViewModel.getCountryName()
    sharedViewModel.countryNameResponse.observe(context as LifecycleOwner) { networkResult ->
        when (networkResult) {
            is NetworkResult.Success -> {
                networkResult.data?.let { countryNameData ->

                    countryNameList = initCountryName(countryName = countryNameData)

//                    Log.d(
//                        "TAG",
//                        "countryNameList: ${countryNameList.aUD}"
//                    )
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
    sharedViewModel.getCurrencyExchangeRate(queries = provideQueriesFree())
    sharedViewModel.currencyExchangeRateResponse.observe(context as LifecycleOwner) { networkResult ->
        when (networkResult) {
            is NetworkResult.Success -> {
                networkResult.data?.let { currencyExchangeRateData ->
                    currencyExchangeRateList = initCurrencyExchangeRate(currencyExchangeRate = currencyExchangeRateData)

//                    Log.d(
//                        "TAG",
//                        "currencyExchangeRateList: ${currencyExchangeRateList.rates}"
//                    )
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

    var currencyDataList = createCurrencyDataList(countryNameList, currencyExchangeRateList)


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

//                                    sharedViewModel.getCurrencyExchangeRate(queries = provideQueriesFree())
//                                    sharedViewModel.currencyExchangeRateResponse.observe(context as LifecycleOwner) { networkResult ->
//                                        when (networkResult) {
//                                            is NetworkResult.Success -> {
//                                                networkResult.data?.let { currencyData ->
//
//                                                    Log.d(
//                                                        "TAG",
//                                                        "currencyExchangeRate: ${currencyData.rates}"
//                                                    )
//
////                                                    if (valueToConvert.value.isEmpty()) {
////                                                        valueToConvert.value = "1.00"
////                                                    }
//                                                    val valueToConvertDouble =
//                                                        valueToConvert.value.toDouble()
//
//                                                    val fromCurrencyValue =
//                                                        convertRatesByCurrencyCode(
//                                                            fromCurrencyCode.value,
//                                                            currencyData.rates
//                                                        )
//
////                                                    val toCurrencyValue =
////                                                        convertRatesByCurrencyCode(
////                                                            toCurrencyCode.value,
////                                                            currencyData.rates
////                                                        )
////
////                                                    convertedValue.value =
////                                                        "${formattedResult(valueToConvertDouble * toCurrencyValue / fromCurrencyValue)} ${toCurrencyCode.value}"
////
////                                                    singleConvertedValue.value =
////                                                        "1 ${fromCurrencyCode.value} = ${
////                                                            formattedResult(
////                                                                toCurrencyValue / fromCurrencyValue
////                                                            )
////                                                        } ${toCurrencyCode.value}"
////
////                                                    Log.d(
////                                                        "TAG",
////                                                        "convertedValue: ${convertedValue.value}"
////                                                    )
////                                                    Log.d(
////                                                        "TAG",
////                                                        "singleConvertedValue: ${singleConvertedValue.value}"
////                                                    )
//
//                                                    // store string in List
//                                                    for (i in currencyDataList.indices) {
//
//                                                        val toCurrencyValue =
//                                                            convertRatesByCurrencyCode(
//                                                                convertCurrencyCodeByOrder(i),
//                                                                currencyData.rates
//                                                            )
//
//                                                        convertedValue.value =
//                                                            "${formattedResult(valueToConvertDouble * toCurrencyValue / fromCurrencyValue)} ${convertCurrencyCodeByOrder(i)}"
//
//                                                        singleConvertedValue.value =
//                                                            "${valueToConvert.value} ${fromCurrencyCode.value} = ${
//                                                                formattedResult(
//                                                                    valueToConvertDouble * toCurrencyValue / fromCurrencyValue
//                                                                )
//                                                            } ${convertCurrencyCodeByOrder(i)}"
//
//                                                        currencyDataList[i].convertedValue = singleConvertedValue.value
//
//                                                        Log.d(
//                                                            "TAG",
//                                                            "convertedValue: ${convertedValue.value}"
//                                                        )
//                                                        Log.d(
//                                                            "TAG",
//                                                            "singleConvertedValue: ${singleConvertedValue.value}"
//                                                        )
//                                                    }
//
//                                                }
//                                            }
//                                            is NetworkResult.Error -> {
//                                                Toast
//                                                    .makeText(
//                                                        context,
//                                                        networkResult.message,
//                                                        Toast.LENGTH_SHORT
//                                                    )
//                                                    .show()
//                                                Log.d("TAG", "Error: ${networkResult.message}")
//                                            }
//                                            is NetworkResult.Loading -> {
//                                                Toast
//                                                    .makeText(
//                                                        context,
//                                                        "Loading...",
//                                                        Toast.LENGTH_SHORT
//                                                    )
//                                                    .show()
//                                            }
//                                        }
//                                    }
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
                onValueChange = {input -> valueToConvert.value = input },
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

        Button(
            onClick = {
                sharedViewModel.getCurrencyExchangeRate(queries = provideQueriesFree())
                sharedViewModel.currencyExchangeRateResponse.observe(context as LifecycleOwner) { networkResult ->
                    when (networkResult) {
                        is NetworkResult.Success -> {
                            networkResult.data?.let { currencyData ->

                                val valueToConvertDouble =
                                    valueToConvert.value.toDouble()

                                val fromCurrencyValue =
                                    convertRatesByCurrencyCode(
                                        fromCurrencyCode.value,
                                        currencyData.rates
                                    )

                                // convert each currency
                                for (i in currencyDataList.indices) {

                                    val toCurrencyValue =
                                        convertRatesByCurrencyCode(
                                            convertCurrencyCodeByOrder(i),
                                            currencyData.rates
                                        )

                                    convertedValue.value =
                                        "${formattedResult(valueToConvertDouble * toCurrencyValue / fromCurrencyValue)} ${convertCurrencyCodeByOrder(i)}"

                                    singleConvertedValue.value =
                                        "${valueToConvert.value} ${fromCurrencyCode.value} = ${
                                            formattedResult(
                                                valueToConvertDouble * toCurrencyValue / fromCurrencyValue
                                            )
                                        } ${convertCurrencyCodeByOrder(i)}"

                                    currencyDataList[i].convertedValue = singleConvertedValue.value
                                    sharedViewModel.currencyDataList.value!![i].convertedValue = singleConvertedValue.value
                                    Log.d("TAG","sharedviewmodel converted: ${sharedViewModel.currencyDataList.value!![i].convertedValue}")

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
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("CONVERT", fontSize = 20.sp, color = Color.White)
        }

        // Grid View
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                cells = GridCells.Adaptive(100.dp),
                content = {
                    items(sharedViewModel.currencyDataList.value!!.size) { item ->

                        GridItem(currencyData = sharedViewModel.currencyDataList.value!![item], onItemClicked = {
                            Toast.makeText(context, it.currencyCode, Toast.LENGTH_SHORT).show()
                        })
                    }
                }
            )
        }
    }
}

@Composable
private fun GridItem(currencyData: CurrencyData, onItemClicked:(currencyData: CurrencyData) -> Unit) {
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
                onClick = { onItemClicked(currencyData) }
            ),
        backgroundColor = MaterialTheme.colors.boxBackgroundColor,
        elevation = 12.dp,
        shape = RoundedCornerShape(6.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = currencyData.countryName, fontSize = 10.sp)
            Text(text = currencyData.convertedValue, fontSize = 8.sp)
        }
    }
}

private fun createCurrencyDataList(countryName: CountryName, currencyExchangeRate: CurrencyExchangeRate): List<CurrencyData> {
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

fun provideQueriesFree(): Map<String, String> {
    val queries = HashMap<String, String>()
    queries.apply {
        this["app_id"] = ConstantValue.APP_ID
    }
    return queries
}

//// only "USD" is provided in free plan, can set [base] in paid plan.
//fun provideQueries(base: String): Map<String, String> {
//    val queries = HashMap<String, String>()
//    queries.apply {
//        this["app_id"] = ConstantValue.APP_ID
////        this["base"] = "USD"
//    }
//    return queries
//}

//@ExperimentalMaterialApi
//@ExperimentalFoundationApi
//@Composable
//@Preview
//private fun ConverterScreenPreview() {
//    ConverterScreen()
//}


fun initCountryName(countryName: CountryName): CountryName {
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

fun initCurrencyExchangeRate(currencyExchangeRate: CurrencyExchangeRate): CurrencyExchangeRate {
    return CurrencyExchangeRate(
        rates = currencyExchangeRate.rates
    )
}

//fun initCurrencyData(countryName: CountryName, currencyExchangeRate: CurrencyExchangeRate): ArrayList<CurrencyData> {
//    val resultList = ArrayList<CurrencyData>()
//    val orderList = countryName.toOrder()
//    val currencyCodeList = countryName.toCurrencyCode()
//
//    resultList.add(CurrencyData(id = orderList.aUD.toInt(), countryName = countryName.aUD, currencyCode = currencyCodeList.aUD, currencyExchangeRate = currencyExchangeRate.rates.aUD))
//    resultList.add(CurrencyData(id = orderList.bGN.toInt(), countryName = countryName.bGN, currencyCode = currencyCodeList.bGN, currencyExchangeRate = currencyExchangeRate.rates.bGN))
//    resultList.add(CurrencyData(id = orderList.bRL.toInt(), countryName = countryName.bRL, currencyCode = currencyCodeList.bRL, currencyExchangeRate = currencyExchangeRate.rates.bRL))
//    resultList.add(CurrencyData(id = orderList.cAD.toInt(), countryName = countryName.cAD, currencyCode = currencyCodeList.cAD, currencyExchangeRate = currencyExchangeRate.rates.cAD))
//    resultList.add(CurrencyData(id = orderList.cHF.toInt(), countryName = countryName.cHF, currencyCode = currencyCodeList.cHF, currencyExchangeRate = currencyExchangeRate.rates.cHF))
//    resultList.add(CurrencyData(id = orderList.cNY.toInt(), countryName = countryName.cNY, currencyCode = currencyCodeList.cNY, currencyExchangeRate = currencyExchangeRate.rates.cNY))
//    resultList.add(CurrencyData(id = orderList.cZK.toInt(), countryName = countryName.cZK, currencyCode = currencyCodeList.cZK, currencyExchangeRate = currencyExchangeRate.rates.cZK))
//    resultList.add(CurrencyData(id = orderList.dKK.toInt(), countryName = countryName.dKK, currencyCode = currencyCodeList.dKK, currencyExchangeRate = currencyExchangeRate.rates.dKK))
//    resultList.add(CurrencyData(id = orderList.eUR.toInt(), countryName = countryName.eUR, currencyCode = currencyCodeList.eUR, currencyExchangeRate = currencyExchangeRate.rates.eUR))
//    resultList.add(CurrencyData(id = orderList.gBP.toInt(), countryName = countryName.gBP, currencyCode = currencyCodeList.gBP, currencyExchangeRate = currencyExchangeRate.rates.gBP))
//    resultList.add(CurrencyData(id = orderList.hKD.toInt(), countryName = countryName.hKD, currencyCode = currencyCodeList.hKD, currencyExchangeRate = currencyExchangeRate.rates.hKD))
//    resultList.add(CurrencyData(id = orderList.hRK.toInt(), countryName = countryName.hRK, currencyCode = currencyCodeList.hRK, currencyExchangeRate = currencyExchangeRate.rates.hRK))
//    resultList.add(CurrencyData(id = orderList.hUF.toInt(), countryName = countryName.hUF, currencyCode = currencyCodeList.hUF, currencyExchangeRate = currencyExchangeRate.rates.hUF))
//    resultList.add(CurrencyData(id = orderList.iDR.toInt(), countryName = countryName.iDR, currencyCode = currencyCodeList.iDR, currencyExchangeRate = currencyExchangeRate.rates.iDR))
//    resultList.add(CurrencyData(id = orderList.iNR.toInt(), countryName = countryName.iNR, currencyCode = currencyCodeList.iNR, currencyExchangeRate = currencyExchangeRate.rates.iNR))
//    resultList.add(CurrencyData(id = orderList.iSK.toInt(), countryName = countryName.iSK, currencyCode = currencyCodeList.iSK, currencyExchangeRate = currencyExchangeRate.rates.iSK))
//    resultList.add(CurrencyData(id = orderList.jPY.toInt(), countryName = countryName.jPY, currencyCode = currencyCodeList.jPY, currencyExchangeRate = currencyExchangeRate.rates.jPY))
//    resultList.add(CurrencyData(id = orderList.kRW.toInt(), countryName = countryName.kRW, currencyCode = currencyCodeList.kRW, currencyExchangeRate = currencyExchangeRate.rates.kRW))
//    resultList.add(CurrencyData(id = orderList.mXN.toInt(), countryName = countryName.mXN, currencyCode = currencyCodeList.mXN, currencyExchangeRate = currencyExchangeRate.rates.mXN))
//    resultList.add(CurrencyData(id = orderList.mYR.toInt(), countryName = countryName.mYR, currencyCode = currencyCodeList.mYR, currencyExchangeRate = currencyExchangeRate.rates.mYR))
//    resultList.add(CurrencyData(id = orderList.nOK.toInt(), countryName = countryName.nOK, currencyCode = currencyCodeList.nOK, currencyExchangeRate = currencyExchangeRate.rates.nOK))
//    resultList.add(CurrencyData(id = orderList.nZD.toInt(), countryName = countryName.nZD, currencyCode = currencyCodeList.nZD, currencyExchangeRate = currencyExchangeRate.rates.nZD))
//    resultList.add(CurrencyData(id = orderList.pHP.toInt(), countryName = countryName.pHP, currencyCode = currencyCodeList.pHP, currencyExchangeRate = currencyExchangeRate.rates.pHP))
//    resultList.add(CurrencyData(id = orderList.pLN.toInt(), countryName = countryName.pLN, currencyCode = currencyCodeList.pLN, currencyExchangeRate = currencyExchangeRate.rates.pLN))
//    resultList.add(CurrencyData(id = orderList.rON.toInt(), countryName = countryName.rON, currencyCode = currencyCodeList.rON, currencyExchangeRate = currencyExchangeRate.rates.rON))
//    resultList.add(CurrencyData(id = orderList.rUB.toInt(), countryName = countryName.rUB, currencyCode = currencyCodeList.rUB, currencyExchangeRate = currencyExchangeRate.rates.rUB))
//    resultList.add(CurrencyData(id = orderList.sEK.toInt(), countryName = countryName.sEK, currencyCode = currencyCodeList.sEK, currencyExchangeRate = currencyExchangeRate.rates.sEK))
//    resultList.add(CurrencyData(id = orderList.sGD.toInt(), countryName = countryName.sGD, currencyCode = currencyCodeList.sGD, currencyExchangeRate = currencyExchangeRate.rates.sGD))
//    resultList.add(CurrencyData(id = orderList.tHB.toInt(), countryName = countryName.tHB, currencyCode = currencyCodeList.tHB, currencyExchangeRate = currencyExchangeRate.rates.tHB))
//    resultList.add(CurrencyData(id = orderList.tRY.toInt(), countryName = countryName.tRY, currencyCode = currencyCodeList.tRY, currencyExchangeRate = currencyExchangeRate.rates.tRY))
//    resultList.add(CurrencyData(id = orderList.tWD.toInt(), countryName = countryName.tWD, currencyCode = currencyCodeList.tWD, currencyExchangeRate = currencyExchangeRate.rates.tWD))
//    resultList.add(CurrencyData(id = orderList.uSD.toInt(), countryName = countryName.uSD, currencyCode = currencyCodeList.uSD, currencyExchangeRate = currencyExchangeRate.rates.uSD))
//    resultList.add(CurrencyData(id = orderList.zAR.toInt(), countryName = countryName.zAR, currencyCode = currencyCodeList.zAR, currencyExchangeRate = currencyExchangeRate.rates.zAR))
//
//    return resultList
//}