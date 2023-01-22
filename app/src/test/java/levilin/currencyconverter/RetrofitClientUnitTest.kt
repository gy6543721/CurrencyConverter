package levilin.currencyconverter

import levilin.currencyconverter.utility.ConstantValue
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test
import org.junit.Assert.*

class RetrofitClientUnitTest {
    @Test
    fun testAPIService() {
        val client = OkHttpClient()
        val appID = ConstantValue.APP_ID
        val request = Request.Builder()
            .url("https://openexchangerates.org/api/latest.json?app_id=${appID}")
            .get()
            .addHeader("accept", "application/json")
            .build()

        val response = client.newCall(request).execute()
        val excepted = 200

        assertEquals(excepted, response.code)
        println(response.body)
    }
}