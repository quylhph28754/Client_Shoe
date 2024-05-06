package com.fpoly.shoes_app.framework.data.module

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import com.fpoly.shoes_app.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object CheckValidate {
    fun checkPhone(context: Context, edtData: TextInputEditText, layoutData: TextInputLayout,button: Button): Boolean {
        return if ((edtData.text?.length ?: 0) < 10) {
            layoutData.error = context.getString(R.string.phone10)
            false
        } else {
            layoutData.error = null
            true
            button.requestFocus()
        }
    }

    fun checkEmail(context: Context, edtData: TextInputEditText, layoutData: TextInputLayout,layoutDataMail: TextInputLayout): Boolean {
        return if ((edtData.text?.length ?: 0) == 0) {
            layoutData.error = context.getString(R.string._email)
            false
        } else if (!isValidEmail(edtData.text)) {
            layoutData.error = context.getString(R.string._formemail)
            false
        } else {
            layoutData.error = null
            true
            layoutDataMail.requestFocus()
        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target!!).matches()
    }
}