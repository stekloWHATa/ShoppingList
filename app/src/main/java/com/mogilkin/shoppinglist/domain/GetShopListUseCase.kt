package com.mogilkin.shoppinglist.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class GetShopListUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {
    fun getShopList(): LiveData<List<ShopItem>>{
        return shopListRepository.getShopList()
    }
}