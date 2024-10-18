package com.mogilkin.shoppinglist.di

import androidx.lifecycle.ViewModel
import com.mogilkin.shoppinglist.presentation.MainViewModel
import com.mogilkin.shoppinglist.presentation.ShopItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ShopListViewModelModule {
    @IntoMap
    @ShopListViewModelKey(ShopItemViewModel::class)
    @Binds
    fun bindShopItemViewModel(impl: ShopItemViewModel) : ViewModel

    @IntoMap
    @ShopListViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(impl: MainViewModel) : ViewModel
}