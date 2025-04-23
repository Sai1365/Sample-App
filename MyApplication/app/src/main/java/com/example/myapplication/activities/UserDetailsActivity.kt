package com.example.myapplication.activities

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.GoogleSignInViewModel
import com.example.myapplication.R

class UserDetailsActivity : AppCompatActivity() {

    private val viewModel: GoogleSignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val nameText = findViewById<TextView>(R.id.tvName)
        val emailText = findViewById<TextView>(R.id.tvEmail)

        viewModel.allUsers.observe(this) { users ->
            users.firstOrNull()?.let { user ->
                nameText.text = user.name
                emailText.text = user.email
            }
        }
    }
}
