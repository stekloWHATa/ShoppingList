package com.mogilkin.shoppinglist.domain


data class ShopItem(
    var id: Int = ID_FOR_GENERATE,
    val name: String,
    val count: Int,
    var enabled: Boolean
)
{
    companion object{
        const val ID_FOR_GENERATE = 0
    }
}
