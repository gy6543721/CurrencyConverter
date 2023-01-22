package levilin.currencyconverter.data.local

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import levilin.currencyconverter.model.local.CurrencyItem

class FakeCurrencyItemDAO: CurrencyItemDAO {

    private val currencyItems = mutableListOf<CurrencyItem>()
    private val observableCurrencyItems = MutableLiveData<List<CurrencyItem>>(currencyItems)

    override suspend fun insertItem(currencyItem: CurrencyItem) {
        currencyItems.add(currencyItem)
        refreshLiveData()
    }

    override fun getAllItems(): Flow<List<CurrencyItem>> {
        currencyItems.add(CurrencyItem())
        val result = MutableStateFlow<List<CurrencyItem>>(ArrayList())
        result.value = currencyItems
        return result
    }

    override suspend fun updateItem(currencyItem: CurrencyItem) {
        for (i in currencyItems.indices) {
            if (currencyItems[i].id == currencyItem.id) {
                currencyItems[i] = currencyItem
            }
        }
        refreshLiveData()
    }

    override suspend fun deleteItem(currencyItem: CurrencyItem) {
        currencyItems.remove(currencyItem)
        refreshLiveData()
    }

    override fun findItemByID(id: Int): Flow<CurrencyItem> {
        val result = MutableStateFlow(CurrencyItem())
        for (i in currencyItems.indices) {
            if (currencyItems[i].id == id) {
                result.value = currencyItems[i]
            }
        }
        return result
    }

    override fun findItemByCountryName(countryName: String): Flow<CurrencyItem> {
        val result = MutableStateFlow(CurrencyItem())
        for (i in currencyItems.indices) {
            if (currencyItems[i].countryName == countryName) {
                result.value = currencyItems[i]
            }
        }
        return result
    }

    override fun findItemByCurrencyCode(currencyCode: String): Flow<CurrencyItem> {
        val result = MutableStateFlow(CurrencyItem())
        for (i in currencyItems.indices) {
            if (currencyItems[i].currencyCode == currencyCode) {
                result.value = currencyItems[i]
            }
        }
        return result
    }

    private fun refreshLiveData() {
        observableCurrencyItems.postValue(currencyItems)
    }
}