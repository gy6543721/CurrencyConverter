package levilin.currencyconverter.utility

import levilin.currencyconverter.model.CurrencyItem

class ConstantValue {
    companion object {
        const val BASE_URL = "https://openexchangerates.org/"
        const val APP_ID = "d48eb6414d6e4d35b7b13929643f08e8"

        val CURRENCY_LIST = listOf(
            CurrencyItem("Australia", "AUD"),
            CurrencyItem("Brazil", "BRL"),
            CurrencyItem("Bulgaria", "BGN"),
            CurrencyItem("Canada", "CAD"),
            CurrencyItem("China", "CNY"),
            CurrencyItem("Croatia", "HRK"),
            CurrencyItem("Czech Republic", "CZK"),
            CurrencyItem("Denmark", "DKK"),
            CurrencyItem("European Union", "EUR"),
            CurrencyItem("Great Britain", "GBP"),
            CurrencyItem("Hong Kong", "HKD"),
            CurrencyItem("Hungary", "HUF"),
            CurrencyItem("Iceland", "ISK"),
            CurrencyItem("India", "INR"),
            CurrencyItem("Indonesia", "IDR"),
            CurrencyItem("Israel", "ILS"),
            CurrencyItem("Japan", "JPY"),
            CurrencyItem("Korea", "KRW"),
            CurrencyItem("Malaysia", "MYR"),
            CurrencyItem("Mexico", "MXN"),
            CurrencyItem("New Zealand", "NZD"),
            CurrencyItem("Norway", "NOK"),
            CurrencyItem("Philippines", "PHP"),
            CurrencyItem("Poland", "PLN"),
            CurrencyItem("Romania", "RON"),
            CurrencyItem("Russia", "RUB"),
            CurrencyItem("Singapore", "SGD"),
            CurrencyItem("South Africa", "ZAR"),
            CurrencyItem("Sweden", "SEK"),
            CurrencyItem("Switzerland", "CHF"),
            CurrencyItem("Taiwan", "TWD"),
            CurrencyItem("Thailand", "THB"),
            CurrencyItem("Turkey", "TRY"),
            CurrencyItem("United States", "USD"),
        )
    }
}