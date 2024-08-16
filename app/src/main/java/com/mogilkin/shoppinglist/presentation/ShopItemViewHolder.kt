package com.mogilkin.shoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mogilkin.shoppinglist.R

class ShopItemViewHolder(view : View): RecyclerView.ViewHolder(view){//в конструктор прилетает
// родительская вью из которой как раз-таки мы и берем все элементы - по типу textV, imageV и т.д.
    val tvName: TextView = view.findViewById(R.id.tv_name) // вуаля тут мы получаем текстВью из родительской вьюшки
    val tvCount: TextView = view.findViewById(R.id.tv_count)
}