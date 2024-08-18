package com.mogilkin.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.mogilkin.shoppinglist.R
import com.mogilkin.shoppinglist.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews()
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId) //получаю конкретный, нужный мне итем
        viewModel.shopItem.observe(this) {
            etName.text =
                Editable.Factory.getInstance().newEditable(it.name)
            etCount.text =
                Editable.Factory.getInstance().newEditable(it.count.toString())

        }
        setupButtonClickListenerForEditMode()
        setupTextChangedListeners()
    }

    private fun launchAddMode() {
        setupButtonClickListenerForAddMode()
        setupTextChangedListeners()

    }

    private fun initViews() {
        tilName = findViewById(R.id.til_name)
        tilCount = findViewById(R.id.til_count)
        etName = findViewById(R.id.et_name)
        etCount = findViewById(R.id.et_count)
        buttonSave = findViewById(R.id.save_button)
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode: $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun setupButtonClickListenerForAddMode(){
        buttonSave.setOnClickListener {
            checkNameForCorrect()
            checkCountForCorrect()
            val inputName = etName.text
            val inputCount = etCount.text
            viewModel.addShopItem(inputName.toString(), inputCount.toString())
            viewModel.canCloseActivity.observe(this){
                finish()
            }
        }
    }

    private fun setupButtonClickListenerForEditMode() {
        buttonSave.setOnClickListener {
            checkNameForCorrect() //при нажатии на кнопку проверяю имя на корректность
            checkCountForCorrect() //сейм хуйня
            viewModel.editShopItem(etName.text.toString(), etCount.text.toString())
            viewModel.canCloseActivity.observe(this) {
                finish()
            }
        }
    }

    private fun checkNameForCorrect() {
        viewModel.errorInputName.observe(this) {
            tilName.error = if (viewModel.errorInputName.value == true){
                getString(R.string.error_name_message)
            } else{
                null
            }
        }
    }

    private fun checkCountForCorrect() {
        viewModel.errorInputCount.observe(this) {
            tilCount.error = if (viewModel.errorInputCount.value == true){
                getString(R.string.error_count_message)
            } else{
                null
            }
        }
    }

    private fun setupTextChangedListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}