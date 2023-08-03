package com.example.workograf20


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class StatisticsActivity : AppCompatActivity() {
/*
    private lateinit var dayTextView: TextView
    private lateinit var weekTextView: TextView
    private lateinit var monthTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private var totalFormattedTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

        // Retrieve the timeInSeconds value from the intent
        val timeInSeconds = intent.getLongExtra("timeInSeconds", 0)
        val formattedTime = formatTime(timeInSeconds)

        // Get the previous totalFormattedTime from SharedPreferences
        val previousTotalTimeInSeconds = sharedPreferences.getLong("totalTimeInSeconds", 0)
        val updatedTotalTimeInSeconds = previousTotalTimeInSeconds + timeInSeconds
        totalFormattedTime = formatTime(updatedTotalTimeInSeconds)

        // Store the updated totalFormattedTime in SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putLong("totalTimeInSeconds", updatedTotalTimeInSeconds)
        editor.apply()

        dayTextView = findViewById(R.id.dayTextView)
        weekTextView = findViewById(R.id.weekTextView)
        monthTextView = findViewById(R.id.monthTextView)
        yearTextView = findViewById(R.id.yearTextView)
        timerTextView = findViewById(R.id.timerTextView)

        dayTextView.text = "This period: $formattedTime"
        weekTextView.text = "Today: $formattedTime"
        monthTextView.text = "This week: $formattedTime"
        yearTextView.text = "This month: $formattedTime"
        timerTextView.text = "Total: $totalFormattedTime"

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
}*/
private lateinit var dayTextView: TextView
    private lateinit var weekTextView: TextView
    private lateinit var monthTextView: TextView
    private lateinit var yearTextView: TextView
private lateinit var timerTextView: TextView
    private lateinit var totalTimeTextView: TextView // Для відображення Total Time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        timerTextView = findViewById(R.id.timerTextView)
        totalTimeTextView = findViewById(R.id.totalTimeTextView)
        dayTextView = findViewById(R.id.dayTextView)
        weekTextView = findViewById(R.id.weekTextView)
        monthTextView = findViewById(R.id.monthTextView)
        yearTextView = findViewById(R.id.yearTextView)

        // Викликаємо асинхронний запит до бази даних
        readDataFromDatabase()

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            // Закриття поточної активності і повернення до попередньої
            finish()
        }
    }

    private fun readDataFromDatabase() {
        val dbReader = DatabaselsIsRead()

        GlobalScope.launch(Dispatchers.IO) {
            val stats = dbReader.getTotalTimeAndStatsFromDatabase()

            // Отримання і виведення даних у TextView на головному потоці
            withContext(Dispatchers.Main) {
                timerTextView.text = "Total Time: ${stats["totalTime"] ?: "N/A"}"
                dayTextView.text = "This period: ${stats["period"] ?: "N/A"}"
                weekTextView.text = "Today: ${stats["today"] ?: "N/A"}"
                monthTextView.text = "This week: ${stats["week"] ?: "N/A"}"
                yearTextView.text = "This month: ${stats["month"] ?: "N/A"}"
            }
        }
    }
}