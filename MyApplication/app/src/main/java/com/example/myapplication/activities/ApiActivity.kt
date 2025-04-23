package com.example.myapplication.activities

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myapplication.ItemAdapter
import com.example.myapplication.ItemViewModelFactory
import com.example.myapplication.R
import com.example.myapplication.api.ApiService
import com.example.myapplication.api.AppDatabase
import com.example.myapplication.api.ItemEntity
import com.example.myapplication.api.ItemRepository
import com.example.myapplication.api.ItemViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiActivity : AppCompatActivity() {
    private lateinit var viewModel: ItemViewModel
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        adapter = ItemAdapter { item ->

            val dialog = CustomDialogFragment.newInstance(item, viewModel)
            dialog.show(supportFragmentManager, "custom_dialog")

//            val dialogFragment = CustomDialogFragment(viewModel).apply {
//                arguments = Bundle().apply {
//                    putString("name", item.name)
//                    putString("data", item.data)
//                }
//            }
//            dialogFragment.show(supportFragmentManager, "CustomDialog")
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "my_db").build()
        val api = Retrofit.Builder()
            .baseUrl("https://api.restful-api.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
        val repository = ItemRepository(api, db.itemDao())
        val factory = ItemViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[ItemViewModel::class.java]

        viewModel.items.observe(this) { items ->
            adapter.submitList(items)
        }

        viewModel.error.observe(this) {
            it?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        viewModel.loadItems()
    }


    class CustomDialogFragment(private val viewModel: ItemViewModel) : DialogFragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.dialog_update_item, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val nameEditText = view.findViewById<EditText>(R.id.editName)
            val dataEditText = view.findViewById<EditText>(R.id.editData)
            val updateButton = view.findViewById<Button>(R.id.updateButton)
            val deleteButton = view.findViewById<Button>(R.id.deleteButton)

            val item = arguments?.getParcelable<ItemEntity>("item")
            if (item == null) {
                dismiss()
                return
            }

            nameEditText.setText(item.name)
            dataEditText.setText(item.data)


            updateButton.setOnClickListener {
                val updatedItem = item.copy(
                    name = nameEditText.text.toString(),
                    data = dataEditText.text.toString()
                )
                viewModel.update(updatedItem)
                dismiss()
            }

            deleteButton.setOnClickListener {
                viewModel.delete(item)
                dismiss()
            }
        }

        companion object {
            fun newInstance(item: ItemEntity, viewModel: ItemViewModel): CustomDialogFragment {
                val args = Bundle().apply {
                    putParcelable("item", item)
                }
                val fragment = CustomDialogFragment(viewModel)
                fragment.arguments = args
                return fragment
            }
        }
    }





}