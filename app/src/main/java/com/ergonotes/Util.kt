package com.ergonotes

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

class Util {}

fun hideKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    // Check if no view has focus
    val currentFocusedView = activity.currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

// class TextItemViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

