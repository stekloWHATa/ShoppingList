package com.mogilkin.shoppinglist.domain

interface ShopListRepository {
    fun addShopItem(shopItem : ShopItem)

    fun editShopItem(shopItem: ShopItem) : ShopItem

    fun getShopItem(shopItemId : Int) : ShopItem

    fun getShopList(): List<ShopItem>

    fun removeShopItem(shopItem: ShopItem)
}