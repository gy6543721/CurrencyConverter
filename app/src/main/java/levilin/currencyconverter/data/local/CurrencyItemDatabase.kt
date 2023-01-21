package levilin.currencyconverter.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import levilin.currencyconverter.model.local.CurrencyItem
import levilin.currencyconverter.model.local.CurrencyItemDAO

@Database(entities = [CurrencyItem::class], version = 1, exportSchema = false)
abstract class CurrencyItemDatabase : RoomDatabase() {
    abstract fun currencyItemDAO(): CurrencyItemDAO

    companion object {

        @Volatile
        private lateinit var instance: CurrencyItemDatabase
        private const val name = "CURRENCY_ITEMS_DATABASE.db"

        fun getInstance(context: Context): CurrencyItemDatabase {
            if (!::instance.isInitialized) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    CurrencyItemDatabase::class.java,
                    name
                )
                .fallbackToDestructiveMigration()
                .build()
            }

            Log.d("TAG", "Database Build: ${instance.currencyItemDAO().getAllItems()}")

            return instance
        }
    }

    fun getDAO(): CurrencyItemDAO {
        return instance.currencyItemDAO()
    }
}

//private lateinit var INSTANCE : CurrencyItemDatabase
//
//fun getCurrencyItemDatabase(context: Context): CurrencyItemDatabase {
//
//    synchronized(CurrencyItemDatabase::class.java) {
//        if (!::INSTANCE.isInitialized) {
//            INSTANCE = Room.databaseBuilder(
//                context.applicationContext,
//                CurrencyItemDatabase::class.java,
//                "CURRENCY_ITEMS_DATABASE"
//            ).build()
//        }
//    }
//
//    Log.d("TAG", "Database Build")
//
//    return INSTANCE
//}