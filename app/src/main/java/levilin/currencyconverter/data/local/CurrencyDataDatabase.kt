package levilin.currencyconverter.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import levilin.currencyconverter.model.CurrencyData
import levilin.currencyconverter.model.local.CurrencyDataDAO
import levilin.currencyconverter.model.local.CurrencyExchangeRateDAO
import levilin.currencyconverter.model.remote.CurrencyExchangeRate

//@Database(entities = [CurrencyData::class], version = 1, exportSchema = false)
abstract class CurrencyDataDatabase : RoomDatabase() {
    abstract fun currencyDataDAO(): CurrencyDataDAO
}

private lateinit var INSTANCE : CurrencyDataDatabase

fun getCurrencyDataDatabase(context: Context): CurrencyDataDatabase {

    synchronized(CurrencyDataDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                CurrencyDataDatabase::class.java,
                "currencyDataDatabase"
            ).build()
        }
    }

    Log.d("TAG", "Database build")

    return INSTANCE
}