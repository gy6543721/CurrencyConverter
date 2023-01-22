package levilin.currencyconverter.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CURRENCY_ITEMS")
data class CurrencyItem(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    var id: Int = 0,
    @ColumnInfo(name = "COUNTRY_NAME")
    var countryName: String = "",
    @ColumnInfo(name = "CURRENCY_CODE")
    var currencyCode: String = "",
    @ColumnInfo(name = "CURRENCY_EXCHANGE_RATE")
    var currencyExchangeRate: Double = 0.00,
    @ColumnInfo(name = "CONVERTED_VALUE")
    var convertedValue: String = "",
    @ColumnInfo(name = "SINGLE_CONVERTED_VALUE")
    var singleConvertedValue: String = ""
)

