package com.mogilkin.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import com.mogilkin.shoppinglist.data.ShopListRepositoryImpl

import com.mogilkin.shoppinglist.domain.EditShopItemUseCase
import com.mogilkin.shoppinglist.domain.GetShopListUseCase
import com.mogilkin.shoppinglist.domain.RemoveShopItemUseCase
import com.mogilkin.shoppinglist.domain.ShopItem

class MainViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl // знаю, что неправильно, когда изучу инъекции - фиксану

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val removeShopItemUseCase = RemoveShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun changeEnableState(shopItem: ShopItem){
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
    }

    fun removeShopItem(shopItem: ShopItem){
        removeShopItemUseCase.removeShopItem(shopItem)
    }

}