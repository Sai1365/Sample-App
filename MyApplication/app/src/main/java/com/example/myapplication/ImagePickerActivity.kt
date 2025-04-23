package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class ImagePickerActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var captureButton: Button
    private lateinit var galleryButton: Button

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)

        imageView = findViewById(R.id.imageView)
        captureButton = findViewById(R.id.captureButton)
        galleryButton = findViewById(R.id.galleryButton)
        setupButtonClickListeners()

        requestCameraAndStoragePermissions {
            setupButtonClickListeners()
        }
    }

    private fun setupButtonClickListeners() {
        captureButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        galleryButton.setOnClickListener {
            dispatchGalleryIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.let {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun dispatchGalleryIntent() {
        Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }.also { galleryIntent ->
            galleryIntent.resolveActivity(packageManager)?.let {
                startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(imageBitmap)
                }
                REQUEST_IMAGE_GALLERY -> {
                    data?.data?.let { uri ->
                        imageView.setImageURI(uri)
                    }
                }
            }
        }
    }
}

// Extension function outside the class
fun FragmentActivity.requestCameraAndStoragePermissions(onGranted: () -> Unit) {
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            onGranted()
        } else {
            // Handle the case where permissions are denied
            // You might want to show a message to the user
        }
    }

    val permissionsToRequest = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    if (permissionsToRequest.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }) {
        onGranted()
    } else {
        requestPermissionLauncher.launch(permissionsToRequest)
    }
}