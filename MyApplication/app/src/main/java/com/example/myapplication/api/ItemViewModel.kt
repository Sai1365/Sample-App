package com.example.myapplication.api

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import kotlinx.coroutines.Dispatchers
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
    fun deleteItem(item: ItemEntity, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(item)

            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
            val isNotificationEnabled = sharedPrefs.getBoolean("notifications_enabled", true)

            if (isNotificationEnabled) {
                sendDeleteNotification(context, item)
            }
        }
    }

//    fun delete(item: ItemEntity) = viewModelScope.launch {
//        repository.deleteItem(item)
//    }

    private fun sendDeleteNotification(context: Context, item: ItemEntity) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "delete_channel", "Item Deletion", NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "delete_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Item Deleted")
            .setContentText("Deleted item: ${item.name}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(item.id, notification)
    }

}
