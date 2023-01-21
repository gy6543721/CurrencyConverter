package levilin.currencyconverter.data.local

import android.util.Log
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import levilin.currencyconverter.model.local.CurrencyItem
import levilin.currencyconverter.model.local.CurrencyItemDAO
import javax.inject.Inject

@ViewModelScoped
class LocalRepository @Inject constructor(private val currencyItemDAO: CurrencyItemDAO) {

    val getAllItems: Flow<List<CurrencyItem>> = currencyItemDAO.getAllItems()

    suspend fun insertItem(currencyItem: CurrencyItem) {
        currencyItemDAO.insertItem(currencyItem = currencyItem)
        Log.d("TAG", "insert item to database: ${currencyItem.countryName}")
    }
    suspend fun updateItem(currencyItem: CurrencyItem) {
        currencyItemDAO.updateItem(currencyItem = currencyItem)
    }
    suspend fun deleteItem(currencyItem: CurrencyItem) {
        currencyItemDAO.deleteItem(currencyItem = currencyItem)
    }

    fun findItemByID(id: Int): Flow<CurrencyItem> {
        return currencyItemDAO.findItemByID(id = id)
    }

    fun findItemByCountryName(countryName: String): Flow<CurrencyItem> {
        return currencyItemDAO.findItemByCountryName(countryName = countryName)
    }

    fun findItemByCurrencyCode(currencyCode: String): Flow<CurrencyItem> {
        return currencyItemDAO.findItemByCurrencyCode(currencyCode = currencyCode)
    }
}