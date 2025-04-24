package com.example.myapplication.api

data class ItemResponse(
    val id: Int,
    val name: String,
    val data: Map<String, Any>?
)
