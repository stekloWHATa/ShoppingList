package com.mogilkin.shoppinglist.presentation

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mogilkin.shoppinglist.R
import com.mogilkin.shoppinglist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it) //устанавливаем новый список в адаптер
        }
        binding.buttonAddShopItem.setOnClickListener {
            if (binding.shopItemContainer == null){
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else{
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }

        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun launchFragment(fragment: Fragment){
        supportFragmentManager.popBackStack() //уберет из бэкстека прошлый фрагмент, если его там нет, то нихуя не сделает
        supportFragmentManager.beginTransaction()
            .add(R.id.shop_item_container, fragment)
            .addToBackStack(null) //для чего в параметрах нужно указывать имя?
            .commit()
    }
    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        with(binding.rvShopList) {
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter
            rvShopList.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            rvShopList.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(binding.rvShopList)
    }

    private fun setupLongClickListener(){
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }
    private fun setupClickListener(){
        shopListAdapter.onShopItemClickListener = {
            if (binding.shopItemContainer == null){
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else{
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }

        }
    }
    private fun setupSwipeListener(rvShopList : RecyclerView){
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,  // работа с движением
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT // какие свайпы можно делать
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // тут ничего не трогаем по умолчанию
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition //присваиваем переменной номер текущего элемента, по которому этот свайп был произведен
                val shopItem = shopListAdapter.currentList[position] //присваиваем переменной значение элемента, который лежит по номеру (position)
                viewModel.removeShopItem(shopItem)
            }
        })
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }
}