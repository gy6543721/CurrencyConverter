package levilin.currencyconverter.model


import com.google.gson.annotations.SerializedName

data class CurrencyExchangeRate(
    @SerializedName("disclaimer")
    val disclaimer: String,
    @SerializedName("license")
    val license: String,
    @SerializedName("base")
    val base: String,
    @SerializedName("rates")
    val rates: Rates,
    @SerializedName("timestamp")
    val timestamp: Int
)