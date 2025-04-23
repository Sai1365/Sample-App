package com.example.myapplication.api

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Entity(tableName = "items")

@Parcelize
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String? = null,
    val data: String? = null,
    val uri: String? = null,
    val intentAction: String? = null
) : Parcelable

class Converters {
    @TypeConverter
    fun fromMap(value: Map<String, Any>?): String =
        Gson().toJson(value)

    @TypeConverter
    fun toMap(value: String): Map<String, Any>? =
        Gson().fromJson(value, object : TypeToken<Map<String, Any>>() {}.type)
}
