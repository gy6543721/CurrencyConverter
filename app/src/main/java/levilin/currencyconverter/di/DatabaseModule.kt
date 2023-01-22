package levilin.currencyconverter.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import levilin.currencyconverter.data.local.CurrencyItemDatabase
import levilin.currencyconverter.model.local.CurrencyItem
import levilin.currencyconverter.data.local.CurrencyItemDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = CurrencyItemDatabase.getInstance(context = context)

    @Singleton
    @Provides
    fun provideDAO(currencyItemDatabase: CurrencyItemDatabase): CurrencyItemDAO = currencyItemDatabase.getDAO()

    @Singleton
    @Provides
    fun provideEntity() = CurrencyItem()
}