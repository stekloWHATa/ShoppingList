package com.mogilkin.shoppinglist.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


    //делаем так, когда понимаем, что объект, который прилетит в observer никак использоваться в активити не будет
    private val _canCloseActivity = MutableLiveData<Unit>()
    val canCloseActivity : LiveData<Unit>
        get() = _canCloseActivity

    private val _errorInputName = MutableLiveData<Boolean>() //для вьюмодели
    val errorInputName : LiveData<Boolean> //для активити
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount : LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem : LiveData<ShopItem>
        get() = _shopItem

    fun getShopItem(shopItemId: Int){
        val item = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = item
    }

    fun addShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid){
            addShopItemUseCase.addShopItem(ShopItem(name, count, true))
            shouldCloseScreen()
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                    editShopItemUseCase.editShopItem(item)
                    shouldCloseScreen()
                }//сработает только тогда, когда там есть значение
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
            _errorInputName.value = true
            result = false
        }
        if (count <= 0){
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName(){
        _errorInputName.value = false
    }
    fun resetErrorInputCount(){
        _errorInputCount.value = false
    }
    private fun shouldCloseScreen(){
        _canCloseActivity.value = Unit
    }
}