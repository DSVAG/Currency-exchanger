package com.dsvag.currencyexchanger.data.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

class KeyBoardUtils(context: Context) : ContextCompat() {
    private val TAG = KeyBoardUtils::class.simpleName

    private val inputMethodManager
            by lazy { getSystemService(context, InputMethodManager::class.java)!! }

    fun hideKeyBoard(view: View?) {
        try {
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        } catch (e: Exception) {
            Log.e(TAG, "closeKeyboard: $e")
        }
    }

    fun showKeyBoard(view: View) {
        try {
            inputMethodManager.showSoftInput(view, 0)
        } catch (e: Exception) {
            Log.e(TAG, "closeKeyboard: $e")
        }
    }
}