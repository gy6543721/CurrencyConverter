package levilin.currencyconverter.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import levilin.currencyconverter.model.remote.Rates

//@Entity
data class CurrencyExchangeRateLocal(
//    @PrimaryKey(autoGenerate = true)
    val rates: Rates,
)