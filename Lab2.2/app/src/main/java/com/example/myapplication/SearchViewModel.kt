package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    val names = listOf("Adel", "Aalaa", "Ahmed", "Mohamed", "Ali", "Mona", "Sara")


    val searchFlow = MutableSharedFlow<String>()

    val searchResultFlow = MutableStateFlow(names)

    init {
        observeSearch()
    }

    private fun observeSearch() {
        viewModelScope.launch {
            searchFlow.collect { query ->
                searchResultFlow.value = names.filter { it.contains(query, ignoreCase = true) }

            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            searchFlow.emit(query)
        }
    }

}