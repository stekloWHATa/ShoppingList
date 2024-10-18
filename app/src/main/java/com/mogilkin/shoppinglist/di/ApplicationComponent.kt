package com.mogilkin.shoppinglist.di

import android.app.Application
import com.mogilkin.shoppinglist.presentation.MainActivity
import com.mogilkin.shoppinglist.presentation.ShopItemFragment
import dagger.BindsInstance
import dagger.Component
@ApplicationScope
@Component(modules = [DataModule::class, ShopListViewModelModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: ShopItemFragment)

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}