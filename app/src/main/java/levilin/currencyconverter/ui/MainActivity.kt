package levilin.currencyconverter.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import dagger.hilt.android.AndroidEntryPoint
import levilin.currencyconverter.ui.screen.ConverterScreen
import levilin.currencyconverter.ui.theme.CurrencyConverterTheme
import levilin.currencyconverter.viewmodel.SharedViewModel

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    @Inject
//    private lateinit var databaseRepository: LocalRepository
//    @Inject
//    private lateinit var currencyItemEntity: CurrencyItem

//    private lateinit var sharedViewModel: SharedViewModel

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConverterTheme {
                ConverterScreen(this, sharedViewModel)
            }
        }
    }
}