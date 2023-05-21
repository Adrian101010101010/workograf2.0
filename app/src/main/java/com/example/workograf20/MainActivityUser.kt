package com.example.workograf20

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView

class MainActivityUser : AppCompatActivity() {
    private var isTimerRunning = false
    private lateinit var timer: CountDownTimer
    private var timeInSeconds = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_user)

        val timerTextView1 = findViewById<TextView>(R.id.timerTextView1)

        val resetButton = findViewById<Button>(R.id.resetButton)
        resetButton.setOnClickListener {
            resetTimer()
        }

        // знаходимо кнопку для запуску / зупинки таймера
        val startStopButton = findViewById<Button>(R.id.startStopButton)
        startStopButton.setOnClickListener {
            if (isTimerRunning) {
                startTimer()
            }
        }

        // Відновлення стану таймера при повторному вході в програму
        if (savedInstanceState != null) {
            isTimerRunning = savedInstanceState.getBoolean("isTimerRunning", false)
            timeInSeconds = savedInstanceState.getLong("timeInSeconds", 0)
            if (isTimerRunning) {
                startTimer()
            } else {
                updateTimer()
            }
        } else {
            updateTimer()
        }
    }

    // розпочинаємо таймер
    private fun startTimer() {
        isTimerRunning = true

        // створюємо новий об'єкт CountDownTimer
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeInSeconds++
                updateTimer()
            }

            override fun onFinish() {}
        }

        timer.start()

    }

    // скидаємо таймер на початок
    private fun resetTimer() {
        timeInSeconds = 0
        updateTimer()
        isTimerRunning = false
        timer.cancel()
    }

    // оновлюємо текст таймера згідно часу, що пройшов
    private fun updateTimer() {
        val timerTextView = findViewById<TextView>(R.id.timerTextView)
        timerTextView.text = formatTime(timeInSeconds)
    }

    // форматуємо час у вигляді "гг:хх:сс"
    private fun formatTime(timeInSeconds: Long): String {
        val hours = timeInSeconds / 3600
        val minutes = (timeInSeconds % 3600) / 60
        return String.format("%02d:%02d", hours, minutes)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isTimerRunning", isTimerRunning)
        outState.putLong("timeInSeconds", timeInSeconds)
    }
}