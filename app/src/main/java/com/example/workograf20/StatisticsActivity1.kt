package com.example.workograf20

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


class StatisticsActivity1 : AppCompatActivity() {private lateinit var dayTextView: TextView
    private lateinit var weekTextView: TextView
    private lateinit var monthTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var timerTextView: TextView
    private var lastFormattedTime1: String = ""
    private var timeInSeconds1 = 0L
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics1)

        sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

        // Retrieve the timeInSeconds1 value from the intent
        val timeInSeconds1 = intent.getLongExtra("timeInSeconds1", 0)
        val formattedTime = formatTime(timeInSeconds1)

        // Check if lastFormattedTime1 is stored in SharedPreferences
        lastFormattedTime1 = sharedPreferences.getString("lastFormattedTime1", "") ?: ""

        if (timeInSeconds1 > 0) {
            // If timeInSeconds1 is greater than 0, update lastFormattedTime1
            lastFormattedTime1 = formattedTime
            // Store lastFormattedTime1 in SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("lastFormattedTime1", lastFormattedTime1)
            editor.apply()
        }

        dayTextView = findViewById(R.id.dayTextView)
        weekTextView = findViewById(R.id.weekTextView)
        monthTextView = findViewById(R.id.monthTextView)
        yearTextView = findViewById(R.id.yearTextView)
        timerTextView = findViewById(R.id.timerTextView)

        dayTextView.text = "This period: $lastFormattedTime1"
        weekTextView.text = "Today: $lastFormattedTime1"
        monthTextView.text = "This week: $lastFormattedTime1"
        yearTextView.text = "This month: $lastFormattedTime1"
        timerTextView.text = "Total: $lastFormattedTime1"

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            // Закриття поточної активності і повернення до попередньої
            finish()
        }
    }

    private fun formatTime(timeInSeconds1: Long): String {
        val hours = timeInSeconds1 / 3600
        val minutes = (timeInSeconds1 % 3600) / 60
        val seconds = timeInSeconds1 % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}