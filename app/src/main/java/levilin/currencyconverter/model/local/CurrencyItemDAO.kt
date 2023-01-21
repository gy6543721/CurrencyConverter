package levilin.currencyconverter.model.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyItemDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(currencyItem: CurrencyItem)

    @Query("SELECT * FROM CURRENCY_ITEMS")
    fun getAllItems(): Flow<List<CurrencyItem>>

    @Query("SELECT * FROM CURRENCY_ITEMS WHERE ID LIKE :id")
    fun findItemByID(id: Int): Flow<CurrencyItem>

    @Query("SELECT * FROM CURRENCY_ITEMS WHERE COUNTRY_NAME LIKE :countryName")
    fun findItemByCountryName(countryName: String): Flow<CurrencyItem>

    @Query("SELECT * FROM CURRENCY_ITEMS WHERE CURRENCY_CODE LIKE :currencyCode")
    fun findItemByCurrencyCode(currencyCode: String): Flow<CurrencyItem>

    @Update
    suspend fun updateItem(currencyItem: CurrencyItem)

    @Delete
    suspend fun deleteItem(currencyItem: CurrencyItem)
}