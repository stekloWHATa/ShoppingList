package com.mogilkin.shoppinglist.di

import android.app.Application
import com.mogilkin.shoppinglist.data.AppDatabase
import com.mogilkin.shoppinglist.data.ShopListDao
import com.mogilkin.shoppinglist.data.ShopListRepositoryImpl
import com.mogilkin.shoppinglist.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    @ApplicationScope
    @Binds
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object{
        @ApplicationScope
        @Provides
        fun provideShopListDao(application: Application): ShopListDao{
            return AppDatabase.getInstance(application).shopListDao()
        }
    }
}