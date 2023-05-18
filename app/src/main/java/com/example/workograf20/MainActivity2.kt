package com.example.workograf20

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity2 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val listView = findViewById<ListView>(R.id.listView)
        val table = mutableListOf(
            mutableListOf("Name", "Age", "City"),
            mutableListOf("John", "30", "New York"),
            mutableListOf("Mary", "25", "London"),
            mutableListOf("Bob", "40", "Paris")
        )
        val adapter = ArrayAdapter(this, R.layout.list_item, table)
        listView.adapter = adapter
    }
}
