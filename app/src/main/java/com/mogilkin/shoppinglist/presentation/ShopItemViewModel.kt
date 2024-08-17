package com.mogilkin.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import com.mogilkin.shoppinglist.data.ShopListRepositoryImpl
import com.mogilkin.shoppinglist.domain.AddShopItemUseCase
import com.mogilkin.shoppinglist.domain.EditShopItemUseCase
import com.mogilkin.shoppinglist.domain.GetShopItemUseCase
import com.mogilkin.shoppinglist.domain.ShopItem

class ShopItemViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl

    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    fun getShopItem(shopItemId: Int){
        val item = getShopItemUseCase.getShopItem(shopItemId)
    }

    fun addShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid){
            addShopItemUseCase.addShopItem(ShopItem(name, count, true))
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            editShopItemUseCase.editShopItem(ShopItem(name, count, true))
        }
    }

    private fun parseName(inputName: String?) : String{
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?) : Int{
        return inputCount?.trim()?.toIntOrNull() ?: 0
    }

    private fun validateInput(name: String, count: Int) : Boolean{
        var result = true
        if (name.isBlank()) {
            //TODO: show error input name
            result = false
        }
        if (count <= 0){
            //TODO: show error input count
            result = false
        }
        return result
    }
}