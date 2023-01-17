package levilin.currencyconverter.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import levilin.currencyconverter.ui.screen.ConverterScreen
import levilin.currencyconverter.ui.theme.CurrencyConverterTheme
import levilin.currencyconverter.viewmodel.SharedViewModel

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConverterTheme {
                sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
                ConverterScreen(this, sharedViewModel)
            }
        }
    }
}