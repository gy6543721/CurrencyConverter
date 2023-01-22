package levilin.currencyconverter.ui

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import levilin.currencyconverter.R
import levilin.currencyconverter.ui.screen.ConverterScreen
import levilin.currencyconverter.viewmodel.SharedViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterialApi
@ExperimentalFoundationApi
class CurrencyConverterTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val sharedViewModel = composeTestRule.activity.viewModels<SharedViewModel>().value

        composeTestRule.setContent {
            ConverterScreen(
                context = context,
                sharedViewModel = sharedViewModel
            )
        }
    }

    @Test
    fun check_app_name() {
        val appName = composeTestRule.activity.getString(R.string.app_name)
        composeTestRule.onNodeWithText(text = appName).assertIsDisplayed()
    }

    @Test
    fun check_from_currency_header() {
        val fromCurrency = composeTestRule.activity.getString(R.string.from_currency_header)
        composeTestRule.onNodeWithText(text = fromCurrency).assertIsDisplayed()
    }

    @Test
    fun check_view_model_data() {
        val resultText = composeTestRule.activity.viewModels<SharedViewModel>().value.allLocalItems.value[0].singleConvertedValue
        Log.d("Test", "single converted value: $resultText")
    }

    @Test
    fun check_input_value() {
        val inputAreaTag = composeTestRule.activity.getString(R.string.test_input_value_textField)
        val clearButtonTag = composeTestRule.activity.getString(R.string.test_clear_value_textField)
        val inputValue = "2"

        composeTestRule.onNodeWithTag(testTag = clearButtonTag).performClick()
        composeTestRule.onNodeWithTag(testTag = inputAreaTag).performTextInput(text = inputValue)
        composeTestRule.onNodeWithTag(testTag = inputAreaTag).assert(hasText(text = inputValue, ignoreCase = true))
    }

    @Test
    fun check_selection_list_show() {
        val fromCurrencyTag = composeTestRule.activity.getString(R.string.test_from_currency_area)
        val selectionListTag = composeTestRule.activity.getString(R.string.test_selection_list)
        composeTestRule.onNodeWithTag(testTag = fromCurrencyTag).performClick()
        composeTestRule.onNodeWithTag(testTag = selectionListTag).assertIsDisplayed()
    }

    @Test
    fun check_selection_list_item_data() {
        val fromCurrencyTag = composeTestRule.activity.getString(R.string.test_from_currency_area)
        val selectionListItemTag = composeTestRule.activity.getString(R.string.test_selection_list_item)
        val value = "Australian Dollar (AUD)"
        composeTestRule.onNodeWithTag(testTag = fromCurrencyTag).performClick()
        composeTestRule.onAllNodesWithTag(testTag = selectionListItemTag).assertAll(hasText(text = value, ignoreCase = true))
    }
}