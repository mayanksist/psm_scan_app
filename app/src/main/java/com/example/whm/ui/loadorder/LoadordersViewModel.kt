package com.example.whm.ui.loadorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoadordersViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is product Fragment"
    }
    val text: LiveData<String> = _text
}
