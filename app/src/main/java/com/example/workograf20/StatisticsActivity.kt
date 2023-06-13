package com.example.workograf20


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.Calendar

class StatisticsActivity : AppCompatActivity() {

    private lateinit var dayTextView: TextView
    private lateinit var weekTextView: TextView
    private lateinit var monthTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var timerTextView: TextView
    private var lastFormattedTime: String = ""
    private var timeInSeconds = 0L
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

        // Retrieve the timeInSeconds value from the intent
        val timeInSeconds = intent.getLongExtra("timeInSeconds", 0)
        val formattedTime = formatTime(timeInSeconds)

        // Check if lastFormattedTime is stored in SharedPreferences
        lastFormattedTime = sharedPreferences.getString("lastFormattedTime", "") ?: ""

        if (timeInSeconds > 0) {
            // If timeInSeconds is greater than 0, update lastFormattedTime
            lastFormattedTime = formattedTime
            // Store lastFormattedTime in SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("lastFormattedTime", lastFormattedTime)
            editor.apply()
        }

        dayTextView = findViewById(R.id.dayTextView)
        weekTextView = findViewById(R.id.weekTextView)
        monthTextView = findViewById(R.id.monthTextView)
        yearTextView = findViewById(R.id.yearTextView)
        timerTextView = findViewById(R.id.timerTextView)

        dayTextView.text = "This period: $lastFormattedTime"
        weekTextView.text = "Today: $lastFormattedTime"
        monthTextView.text = "This week: $lastFormattedTime"
        yearTextView.text = "This month: $lastFormattedTime"
        timerTextView.text = "Total: $lastFormattedTime"

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            // Закриття поточної активності і повернення до попередньої
            finish()
        }
    }

    private fun formatTime(timeInSeconds: Long): String {
        val hours = timeInSeconds / 3600
        val minutes = (timeInSeconds % 3600) / 60
        val seconds = timeInSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}