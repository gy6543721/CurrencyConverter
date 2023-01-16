package levilin.currencyconverter.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.Gson
import levilin.currencyconverter.data.CurrencyAPI
import levilin.currencyconverter.model.CurrencyAbbreviation
import levilin.currencyconverter.ui.screen.ConverterScreen
import levilin.currencyconverter.ui.theme.CurrencyConverterTheme
import levilin.currencyconverter.utility.ConstantValue.Companion.BASE_URL
import levilin.currencyconverter.utility.OkHttpUtility
import levilin.currencyconverter.utility.OkHttpUtility.Companion.mOkHttpUtility
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

@ExperimentalMaterialApi
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConverterTheme {
                ConverterScreen()
            }
        }
    }
}