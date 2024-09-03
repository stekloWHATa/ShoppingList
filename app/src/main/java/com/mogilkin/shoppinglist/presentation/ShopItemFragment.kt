package com.mogilkin.shoppinglist.presentation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.mogilkin.shoppinglist.R
import com.mogilkin.shoppinglist.databinding.FragmentShopItemBinding
import com.mogilkin.shoppinglist.domain.ShopItem

class ShopItemFragment() : Fragment() {
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var onEditingFinishedListener : OnEditingFinishedListener

    private var screenMode : String = MODE_UNKNOWN
    private var shopItemId : Int = ShopItem.UNDEFINED_ID

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        parseParams()
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {//в качестве параметра прилетает та активити, к которой прикреплен фрагмент
        super.onAttach(context)
        if (context is OnEditingFinishedListener){
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {//в качестве параметра как раз и прилетает вью, которую мы создаем в onCreateView
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }


    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId) //получаю конкретный, нужный мне итем
        setupButtonClickListenerForEditMode()
        setupTextChangedListeners()
    }

    private fun launchAddMode() {
        setupButtonClickListenerForAddMode()
        setupTextChangedListeners()

    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode: $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun setupButtonClickListenerForAddMode() {
        binding.saveButton.setOnClickListener {
            val inputName = binding.etName.text
            val inputCount = binding.etCount.text
            viewModel.addShopItem(inputName.toString(), inputCount.toString())
            viewModel.canCloseActivity.observe(viewLifecycleOwner) {
                onEditingFinishedListener.onEditingFinished()
            }
        }
    }

    private fun setupButtonClickListenerForEditMode() {
        binding.saveButton.setOnClickListener {
            viewModel.editShopItem(binding.etName.text.toString(), binding.etCount.text.toString())
            viewModel.canCloseActivity.observe(viewLifecycleOwner) {
                onEditingFinishedListener.onEditingFinished()
            }
        }
    }



    private fun setupTextChangedListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    interface OnEditingFinishedListener{
        fun onEditingFinished()
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment{
            return ShopItemFragment().apply { //создаем объект фрагмента
                arguments = Bundle().apply { //создаем объект Bundle, после чего вызываем метод putString
                    putString(SCREEN_MODE, MODE_ADD)
                }
            } //и после всего этого возвращаем экземпляр объекта с данными аргументами
        }
        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment{
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }

    }
}
