package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CounterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)

    val count = _count

    private val _event = MutableSharedFlow<String>()
    val event = _event


    fun increase(){
        _count.value++

        viewModelScope.launch {
            _event.emit("Count increased")
        }
    }

    fun decrease(){
        _count.value--

        viewModelScope.launch {
            _event.emit("Count decreased")
        }

    }

}