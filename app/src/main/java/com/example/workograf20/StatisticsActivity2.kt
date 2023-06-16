package com.example.workograf20

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class StatisticsActivity2 : AppCompatActivity() {
    private lateinit var dayTextView: TextView
    private lateinit var weekTextView: TextView
    private lateinit var monthTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var timerTextView: TextView
    private var lastFormattedTime2: String = ""
    private var timeInSeconds2 = 0L
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics2)

        sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

        // Retrieve the timeInSeconds2 value from the intent
        val timeInSeconds2 = intent.getLongExtra("timeInSeconds2", 0)
        val formattedTime = formatTime(timeInSeconds2)

        // Check if lastFormattedTime2 is stored in SharedPreferences
        lastFormattedTime2 = sharedPreferences.getString("lastFormattedTime2", "") ?: ""

        if (timeInSeconds2 > 0) {
            // If timeInSeconds2 is greater than 0, update lastFormattedTime
            lastFormattedTime2 = formattedTime
            // Store lastFormattedTime2 in SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("lastFormattedTime2", lastFormattedTime2)
            editor.apply()
        }

        dayTextView = findViewById(R.id.dayTextView)
        weekTextView = findViewById(R.id.weekTextView)
        monthTextView = findViewById(R.id.monthTextView)
        yearTextView = findViewById(R.id.yearTextView)
        timerTextView = findViewById(R.id.timerTextView)

        dayTextView.text = "This period: $lastFormattedTime2"
        weekTextView.text = "Today: $lastFormattedTime2"
        monthTextView.text = "This week: $lastFormattedTime2"
        yearTextView.text = "This month: $lastFormattedTime2"
        timerTextView.text = "Total: $lastFormattedTime2"

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            // Закриття поточної активності і повернення до попередньої
            finish()
        }
    }

    private fun formatTime(timeInSeconds2: Long): String {
        val hours = timeInSeconds2 / 3600
        val minutes = (timeInSeconds2 % 3600) / 60
        val seconds = timeInSeconds2 % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}