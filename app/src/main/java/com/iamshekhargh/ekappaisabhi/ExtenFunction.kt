package com.iamshekhargh.ekappaisabhi

import androidx.appcompat.widget.SearchView

/**
 * Created by <<-- iamShekharGH -->>
 * on 01 April 2021
 * at 5:18 PM.
 */

inline fun SearchView.onTextEntered(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}
