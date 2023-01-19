package levilin.currencyconverter.model.remote

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class CurrencyExchangeRate(
//    @SerializedName("disclaimer")
//    val disclaimer: String,
//    @SerializedName("license")
//    val license: String,
//    @SerializedName("base")
//    val base: String,
    @SerializedName("rates")
    val rates: Rates,
//    @SerializedName("timestamp")
//    val timestamp: String
)

fun CurrencyExchangeRate(): CurrencyExchangeRate {
    return CurrencyExchangeRate(
        rates = Rates()
    )
}