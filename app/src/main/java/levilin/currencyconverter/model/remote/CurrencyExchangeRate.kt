package levilin.currencyconverter.model.remote

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class CurrencyExchangeRate(
    @SerializedName("rates")
    val rates: Rates
)

fun CurrencyExchangeRate(): CurrencyExchangeRate {
    return CurrencyExchangeRate(
        rates = Rates()
    )
}