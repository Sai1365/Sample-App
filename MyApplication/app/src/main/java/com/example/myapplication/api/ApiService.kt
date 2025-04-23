package com.example.myapplication.api

import retrofit2.http.GET

interface ApiService {
    @GET("objects")
    suspend fun fetchItems(): List<ItemResponse>
}
