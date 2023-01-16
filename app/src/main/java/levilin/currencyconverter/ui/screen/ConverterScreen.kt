package levilin.currencyconverter.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import kotlinx.coroutines.launch
import levilin.currencyconverter.R
import levilin.currencyconverter.ui.theme.*
import levilin.currencyconverter.utility.ConstantValue.Companion.CURRENCY_LIST

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun ConverterScreen() {

    // Scaffold Control
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    // Focus Control
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val fromCurrencyCode: MutableState<String> = remember { mutableStateOf("USD") }
    val toCurrencyCode: MutableState<String> = remember { mutableStateOf("JPY") }
    var valueToConvert: MutableState<String> = remember { mutableStateOf("") }

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
                            text = "${item.currencyCode}\t ${item.countryName}",
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .clickable {
                                    fromCurrencyCode.value = item.currencyCode
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

        // Grid View
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                cells = GridCells.Adaptive(100.dp),
                content = {
                    items(CURRENCY_LIST.size-1) { item ->
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colors.boxBackgroundColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "$item")
                        }
                    }
                }
            )
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
@Preview
private fun ConverterScreenPreview() {
    ConverterScreen()
}