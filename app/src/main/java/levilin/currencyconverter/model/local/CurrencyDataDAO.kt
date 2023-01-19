package levilin.currencyconverter.model.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import levilin.currencyconverter.model.CurrencyData

//@Dao
interface CurrencyDataDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItems(currencyData: CurrencyData)

    @Query("SELECT * FROM CurrencyData")
    fun getAllDataSet(): LiveData<List<CurrencyData>>

}