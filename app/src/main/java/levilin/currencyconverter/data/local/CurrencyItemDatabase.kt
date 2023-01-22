package levilin.currencyconverter.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import levilin.currencyconverter.model.local.CurrencyItem

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
            return instance
        }
    }

    fun getDAO(): CurrencyItemDAO {
        return instance.currencyItemDAO()
    }
}