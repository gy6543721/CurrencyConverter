package levilin.currencyconverter

import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test
import org.junit.Assert.*

class RetrofitClientUnitTest {
    @Test
    fun testAPIService() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://openexchangerates.org/api/latest.json?app_id=d48eb6414d6e4d35b7b13929643f08e8")
            .get()
            .addHeader("accept", "application/json")
            .build()

        val response = client.newCall(request).execute()
        val excepted = 200

        assertEquals(excepted, response.code)
        println(response.body)
    }
}