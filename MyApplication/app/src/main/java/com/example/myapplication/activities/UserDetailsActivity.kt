package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.GoogleSignInViewModel
import com.example.myapplication.PdfViewer
import com.example.myapplication.R

class UserDetailsActivity : AppCompatActivity() {

    private val viewModel: GoogleSignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val nameText = findViewById<TextView>(R.id.tvUserName)

        viewModel.allUsers.observe(this) { users ->
            users.firstOrNull()?.let { user ->
                nameText.text = "Welcome, ${user.name}"

            }
        }

        findViewById<TextView>(R.id.btnApi).setOnClickListener {
            startActivity(Intent(this, ApiActivity::class.java))

        }
        findViewById<TextView>(R.id.btnPdfViewer).setOnClickListener {
            startActivity(Intent(this, PdfViewer::class.java))
        }
        findViewById<TextView>(R.id.btnImageCapture).setOnClickListener {
            startActivity(Intent(this, ImagePickerActivity::class.java))
        }
    }
}
