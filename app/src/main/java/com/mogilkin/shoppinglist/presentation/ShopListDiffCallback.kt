package com.mogilkin.shoppinglist.presentation

import androidx.recyclerview.widget.DiffUtil
import com.mogilkin.shoppinglist.domain.ShopItem

class ShopListDiffCallback(
    private val oldList : List<ShopItem>,
    private val newList : List<ShopItem>
) : DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // благодаря данному методу адаптер вдупляет, что этот тот же самый элемент, просто случилось
        // какое-то изменение (к примеру, поменялась позиция)
       return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // оке, благодаря прошлому методу мы поняли, что это тот же элемент, теперь мы должны понять,
        // есть ли в нем какие-либо изменения, дабы РВ понял, нужно его перерисовывать или нет
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}