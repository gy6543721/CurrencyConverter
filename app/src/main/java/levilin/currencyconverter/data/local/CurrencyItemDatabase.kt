package levilin.currencyconverter.data.local

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import levilin.currencyconverter.model.local.CurrencyItemDAO

//@Database(entities = [CurrencyItem::class], version = 1, exportSchema = false)
abstract class CurrencyItemDatabase : RoomDatabase() {
    abstract fun currencyDataDAO(): CurrencyItemDAO
}

private lateinit var INSTANCE : CurrencyItemDatabase

fun getCurrencyItemDatabase(context: Context): CurrencyItemDatabase {

    synchronized(CurrencyItemDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                CurrencyItemDatabase::class.java,
                "currencyItemDatabase"
            ).build()
        }
    }

    Log.d("TAG", "Database build")

    return INSTANCE
}