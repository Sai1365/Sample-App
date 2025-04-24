package com.example.myapplication.api

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class ItemRepository(
    private val apiService: ApiService,
    private val dao: ItemDao
) {
    val items: Flow<List<ItemEntity>> = dao.getAllItems()

    suspend fun refreshItems() {
        try {
            val response = apiService.fetchItems()
            val entities = response.map {
                ItemEntity(it.id, it.name, Gson().toJson(it.data))
            }
            dao.insertItems(entities)
        } catch (e: Exception) {
            throw e // Handle in ViewModel
        }
    }

    suspend fun updateItem(item: ItemEntity) = dao.updateItem(item)
    suspend fun deleteItem(item: ItemEntity) = dao.deleteItem(item)
}
