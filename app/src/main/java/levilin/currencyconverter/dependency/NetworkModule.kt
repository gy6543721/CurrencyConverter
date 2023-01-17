package levilin.currencyconverter.dependency

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import levilin.currencyconverter.data.CurrencyAPI
import levilin.currencyconverter.utility.ConstantValue
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory):Retrofit {
        return Retrofit.Builder()
            .baseUrl(ConstantValue.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory():GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient():OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideCurrencyExchangeRateAPI(retrofit: Retrofit):CurrencyAPI {
        return retrofit.create(CurrencyAPI::class.java)
    }

}