package com.example.myapplication.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ItemViewModel(private val repository: ItemRepository) : ViewModel() {
    val items = repository.items.asLiveData()

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadItems() {
        viewModelScope.launch {
            try {
                repository.refreshItems()
            } catch (e: Exception) {
                _error.value = "Failed to fetch data: ${e.message}"
            }
        }
    }

    fun update(item: ItemEntity) = viewModelScope.launch {
        if (item.name?.isNotBlank() == true) {
            repository.updateItem(item)
        } else {
            _error.value = "Name can't be empty"
        }
    }

    fun delete(item: ItemEntity) = viewModelScope.launch {
        repository.deleteItem(item)
    }
}
