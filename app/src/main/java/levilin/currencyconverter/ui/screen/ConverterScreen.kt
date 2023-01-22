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
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import levilin.currencyconverter.R
import levilin.currencyconverter.model.local.CurrencyItem
import levilin.currencyconverter.ui.theme.*
import levilin.currencyconverter.utility.NetworkResult
import levilin.currencyconverter.viewmodel.SharedViewModel
import kotlin.time.Duration.Companion.seconds

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

    // SharedViewModel Data
    val allLocalItems by sharedViewModel.allLocalItems.collectAsState()
    val fromCurrencyCode by sharedViewModel.fromCurrencyCode
    val valueToConvert by sharedViewModel.valueToConvert
    val rawAPICurrencyExchangeRate by sharedViewModel.rawAPICurrencyExchangeRate

    // Timer
    var timer by remember { mutableStateOf(0) }
    var updateFlag by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while(true) {
            delay(1.seconds)
            timer++
        }
    }

    // Selection menu
    BottomSheetScaffold(
        sheetContent = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(allLocalItems) { item ->
                        Text(
                            text = "${item.countryName}\t (${item.currencyCode})",
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .clickable {
                                    sharedViewModel.fromCurrencyCode.value = item.currencyCode

//                                    Log.d("TAG", "sharedviewmodel fromcurrencycode: ${sharedViewModel.fromCurrencyCode.value}")

                                    scope.launch {
                                        scaffoldState.bottomSheetState.collapse()
                                    }
                                },
                            color = MaterialTheme.colors.converterScreenTextColor
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
                    color = MaterialTheme.colors.contentTextColor,
                    style = TextStyle(
                        fontWeight = FontWeight.Black,
                        fontSize = 30.sp
                    )
                )
            },
            backgroundColor = MaterialTheme.colors.boxBackgroundColor,
            elevation = 0.dp
        ) },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetElevation = 6.dp,
        sheetBackgroundColor = MaterialTheme.colors.converterScreenBackgroundColor,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
    ) {
        // Value Input Area
        Column(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 5.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.value_input_header), color = MaterialTheme.colors.converterScreenTextColor)
                Text(text = sharedViewModel.fromCurrencyCode.value, color = MaterialTheme.colors.converterScreenTextColor)
            }

            Spacer(modifier = Modifier.padding(3.dp))

            OutlinedTextField(
                value = valueToConvert,
                onValueChange = { input ->
                    sharedViewModel.valueToConvert.value = input
//                    Log.d("TAG", "sharedviewmodel valuetoconvert: ${sharedViewModel.valueToConvert.value}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .testTag(stringResource(id = R.string.test_input_value_textField)),
                placeholder = {
                    Text(text = valueToConvert, style = MaterialTheme.typography.body1)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { sharedViewModel.valueToConvert.value = "" }, modifier = Modifier.testTag(stringResource(id = R.string.test_clear_value_textField))) {
                        Icon(modifier = Modifier.alpha(ContentAlpha.medium), imageVector = Icons.Filled.Close, contentDescription = stringResource(id = R.string.trail_icon_text), tint = MaterialTheme.colors.boxIconColor)
                    }
                }
            )
        }

        // Selection List Area
        Column(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.from_currency_header), color = MaterialTheme.colors.converterScreenTextColor, modifier = Modifier.testTag(stringResource(id = R.string.test_from_currency_area)))
            Spacer(modifier = Modifier.padding(3.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .focusRequester(focusRequester)
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
                    .testTag(stringResource(id = R.string.test_selection_list))
            ) {
                Text(text = fromCurrencyCode, modifier = Modifier.padding(10.dp).testTag(stringResource(id = R.string.test_selection_list_item)), color = MaterialTheme.colors.converterScreenTextColor)
            }

            Spacer(modifier = Modifier.padding(15.dp))

            // Convert Button
            Button(
                onClick = {
                    focusManager.clearFocus()

                    if (timer > 30 * 60) {
                        updateFlag = true
                    }

                    if (updateFlag) {
                        sharedViewModel.currencyExchangeRateResponse.observe(context as LifecycleOwner) { networkResult ->
                            when (networkResult) {
                                is NetworkResult.Success -> {
                                    networkResult.data?.let { currencyData ->
                                        // Update when timer reach 30min
                                        sharedViewModel.rawAPICurrencyExchangeRate.value = currencyData
                                        sharedViewModel.getExchangeRateResult(rawAPICurrencyExchangeRate)
                                        sharedViewModel.updateDatabase()
                                        timer = 0
                                        updateFlag = false
                                    }
                                }
                                is NetworkResult.Error -> {
                                    Toast.makeText(context, networkResult.message, Toast.LENGTH_SHORT).show()
                                    Log.d("TAG", "Error: ${networkResult.message}")
                                }
                                is NetworkResult.Loading -> {
                                    Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        sharedViewModel.getExchangeRateResult(rawAPICurrencyExchangeRate)
                        sharedViewModel.updateDatabase()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.boxBackgroundColor)
            ) {
                Text(stringResource(id = R.string.convert_button_text), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.contentTextColor)
            }
        }

        // Grid View
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                cells = GridCells.Adaptive(100.dp),
                content = {
                    items(allLocalItems.size) { item ->
                        GridItem(
                            currencyItem = allLocalItems[item],
                            onItemClicked = { currencyItem->
                                if (currencyItem.singleConvertedValue.isNotEmpty() && currencyItem.singleConvertedValue != "") {
                                    Toast.makeText(context, currencyItem.singleConvertedValue, Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
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
            .padding(3.dp)
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
            Text(text = currencyItem.countryName, fontSize = 10.sp, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colors.contentTextColor)
            Text(text = "(${currencyItem.currencyCode})", fontSize = 10.sp, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colors.contentTextColor)
            Spacer(modifier = Modifier.padding(10.dp))
            Text(text = currencyItem.convertedValue, fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colors.contentTextColor)
        }
    }
}