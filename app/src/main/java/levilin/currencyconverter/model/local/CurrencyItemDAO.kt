package levilin.currencyconverter.model.local

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import levilin.currencyconverter.model.CurrencyItem

//@Dao
interface CurrencyItemDAO {
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItems(currencyItem: CurrencyItem)

//    @Query("SELECT * FROM CurrencyItem")
    fun getAllDataSet(): LiveData<List<CurrencyItem>>

}