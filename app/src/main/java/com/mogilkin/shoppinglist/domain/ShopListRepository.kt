package com.mogilkin.shoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {
    fun addShopItem(shopItem : ShopItem)

    fun editShopItem(shopItem: ShopItem)

    fun getShopItem(shopItemId : Int) : ShopItem

    fun getShopList(): LiveData<List<ShopItem>>

    fun removeShopItem(shopItem: ShopItem)
}