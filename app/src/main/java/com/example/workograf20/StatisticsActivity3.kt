package com.example.workograf20

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class StatisticsActivity3 : AppCompatActivity() {
    private lateinit var dayTextView: TextView
    private lateinit var weekTextView: TextView
    private lateinit var monthTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var timerTextView: TextView
    private var lastFormattedTime3: String = ""
    private var timeInSeconds3 = 0L
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics3)

        sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

        // Retrieve the timeInSeconds3 value from the intent
        val timeInSeconds3 = intent.getLongExtra("timeInSeconds3", 0)
        val formattedTime = formatTime(timeInSeconds3)

        // Check if lastFormattedTime3 is stored in SharedPreferences
        lastFormattedTime3 = sharedPreferences.getString("lastFormattedTime3", "") ?: ""

        if (timeInSeconds3 > 0) {
            // If timeInSeconds3 is greater than 0, update lastFormattedTime
            lastFormattedTime3 = formattedTime
            // Store lastFormattedTime3 in SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("lastFormattedTime3", lastFormattedTime3)
            editor.apply()
        }

        dayTextView = findViewById(R.id.dayTextView)
        weekTextView = findViewById(R.id.weekTextView)
        monthTextView = findViewById(R.id.monthTextView)
        yearTextView = findViewById(R.id.yearTextView)
        timerTextView = findViewById(R.id.timerTextView)

        dayTextView.text = "This period: $lastFormattedTime3"
        weekTextView.text = "Today: $lastFormattedTime3"
        monthTextView.text = "This week: $lastFormattedTime3"
        yearTextView.text = "This month: $lastFormattedTime3"
        timerTextView.text = "Total: $lastFormattedTime3"

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            // Закриття поточної активності і повернення до попередньої
            finish()
        }
    }

    private fun formatTime(timeInSeconds3: Long): String {
        val hours = timeInSeconds3 / 3600
        val minutes = (timeInSeconds3 % 3600) / 60
        val seconds = timeInSeconds3 % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}