package com.example.workograf20

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsEmployer : AppCompatActivity() {

    private lateinit var dayTextView: TextView
    private lateinit var weekTextView: TextView
    private lateinit var monthTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var totalTimeTextView: TextView // Для відображення Total Time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_employer2)

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
            val stats = dbReader.getTotalTimeAndStatsFromDatabaseEmployer()

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