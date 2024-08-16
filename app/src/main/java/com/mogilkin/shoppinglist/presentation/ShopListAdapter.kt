package com.mogilkin.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mogilkin.shoppinglist.R
import com.mogilkin.shoppinglist.domain.ShopItem

class ShopListAdapter :
    ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {
    //в дженерике
    // передаем сначала тип, который использует РВ, затем вьюхолдер и в конструкторе коллбек
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(view) //теперь мы возвращаем эту родительскую вью
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position) //получаем элемент по позиции
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count
        holder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        holder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    companion object {
        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = -1
        const val MAX_POOL_SIZE = 10
    }
}