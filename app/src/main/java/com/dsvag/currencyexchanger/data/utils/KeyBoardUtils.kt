package com.dsvag.currencyexchanger.data.utils

import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyBoardUtils(private val inputMethodManager: InputMethodManager) {
    private val TAG = KeyBoardUtils::class.simpleName

    fun hideKeyBoard(view: View) {
        try {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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