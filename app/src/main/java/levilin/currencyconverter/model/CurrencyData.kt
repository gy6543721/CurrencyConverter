package levilin.currencyconverter.model


import com.google.gson.annotations.SerializedName

data class CurrencyData(
    @SerializedName("base")
    val base: String,
    @SerializedName("rates")
    val rates: Rates,
    @SerializedName("timestamp")
    val timestamp: Int
)