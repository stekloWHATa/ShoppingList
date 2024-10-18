package com.mogilkin.shoppinglist.presentation

import android.app.Application
import com.mogilkin.shoppinglist.di.DaggerApplicationComponent

class ShopApp: Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}