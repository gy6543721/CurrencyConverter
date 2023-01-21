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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.launch
import levilin.currencyconverter.R
import levilin.currencyconverter.model.local.CurrencyItem
import levilin.currencyconverter.ui.theme.*
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

    // init API data
    sharedViewModel.getCurrencyDataList()

    val allLocalItems by sharedViewModel.allLocalItems.collectAsState()


    // Selection menu
    BottomSheetScaffold(
        sheetContent = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(sharedViewModel.currencyItemList.value!!) { item ->
                        Text(
                            text = "${item.countryName}\t (${item.currencyCode})",
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .clickable {
                                    sharedViewModel.fromCurrencyCode.value = item.currencyCode
                                    Log.d(
                                        "TAG",
                                        "sharedviewmodel fromcurrencycode: ${sharedViewModel.fromCurrencyCode.value}"
                                    )

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
                .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Amount", color = MaterialTheme.colors.converterScreenTextColor)
                Text(text = sharedViewModel.fromCurrencyCode.value, color = MaterialTheme.colors.converterScreenTextColor)
            }

            Spacer(modifier = Modifier.padding(3.dp))

            OutlinedTextField(
                value = sharedViewModel.valueToConvert.value,
                onValueChange = { input ->
                    sharedViewModel.valueToConvert.value = input
                    Log.d("TAG", "sharedviewmodel valuetoconvert: ${sharedViewModel.valueToConvert.value}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = {
                    Text(text = sharedViewModel.valueToConvert.value, style = MaterialTheme.typography.body1)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { sharedViewModel.valueToConvert.value = "" }) {
                        Icon(modifier = Modifier.alpha(ContentAlpha.medium), imageVector = Icons.Filled.Close, contentDescription = stringResource(id = R.string.trail_icon_text), tint = MaterialTheme.colors.boxIconColor)
                    }
                }
            )
        }

        // Selection List
        Column(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
                .fillMaxWidth()
        ) {
            Text(text = "From", color = MaterialTheme.colors.converterScreenTextColor)
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
            ) {
                Text(text = sharedViewModel.fromCurrencyCode.value, modifier = Modifier.padding(10.dp), color = MaterialTheme.colors.converterScreenTextColor)
            }

            Spacer(modifier = Modifier.padding(15.dp))

            // Convert Button
            Button(
                onClick = {
                    focusManager.clearFocus()

                    sharedViewModel.currencyExchangeRateResponse.observe(context as LifecycleOwner) { networkResult ->
                        when (networkResult) {
                            is NetworkResult.Success -> {
                                networkResult.data?.let { currencyData ->
                                    sharedViewModel.getExchangeRateResult(currencyData)
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
                    items(sharedViewModel.currencyItemList.value!!.size) { item ->
                        GridItem(
                            currencyItem = sharedViewModel.currencyItemList.value!![item],
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
            Text(text = currencyItem.countryName, fontSize = 10.sp, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = "(${currencyItem.currencyCode})", fontSize = 10.sp, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = currencyItem.convertedValue, fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}