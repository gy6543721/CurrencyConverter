package levilin.currencyconverter.model.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import levilin.currencyconverter.model.remote.CurrencyExchangeRate

//
//@Dao
interface CurrencyExchangeRateDAO {
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insertItems(currencyExchangeRateLocal: CurrencyExchangeRate)
//
//    @Query("SELECT * FROM CurrencyExchangeRate")
//    fun getAllDataSet(): LiveData<List<CurrencyExchangeRate>>
//
}