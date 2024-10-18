package com.mogilkin.shoppinglist.presentation

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.mogilkin.shoppinglist.R

@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, isError: Boolean){
    textInputLayout.error = if (isError) {
        textInputLayout.context.getString(R.string.error_name_message)
    } else {
        null
    }
}
@BindingAdapter("errorInputCount")
fun bindErrorInputCount(textInputLayout: TextInputLayout, isError: Boolean){
    textInputLayout.error = if (isError) {
        textInputLayout.context.getString(R.string.error_count_message)
    } else {
        null
    }
}