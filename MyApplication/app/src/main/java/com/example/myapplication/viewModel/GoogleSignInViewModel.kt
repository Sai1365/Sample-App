package com.example.myapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.User
import com.example.myapplication.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoogleSignInViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = UserDatabase.getDatabase(application).userDao()
    val allUsers: LiveData<List<User>> = userDao.getAllUsers()

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.insertUser(user)
        }
    }
}
