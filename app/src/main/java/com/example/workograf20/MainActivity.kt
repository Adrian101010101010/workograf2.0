package com.example.workograf20

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private var isTimerRunning = false
    private lateinit var timer: CountDownTimer
    private var timeInSeconds = 0L


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1: Button = findViewById(R.id.button1)

        button1.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        val button2: Button = findViewById(R.id.button2)


        button2.setOnClickListener {
            val intent = Intent(this, MainActivityRegister::class.java)
            startActivity(intent)
        }

        val timerTextView = findViewById<TextView>(R.id.timerTextView)

        val resetButton = findViewById<Button>(R.id.resetButton)
        resetButton.setOnClickListener {
            resetTimer()
        }

        // знаходимо кнопку для запуску / зупинки таймера
        val startStopButton = findViewById<Button>(R.id.startStopButton)
        startStopButton.setOnClickListener {
            if (isTimerRunning) {
                stopTimer()
            } else {
                startTimer()
            }
        }

        // задаємо початковий вигляд таймера
        timerTextView.text = formatTime(timeInSeconds)
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

        // змінюємо текст кнопки на "Stop"
        val startStopButton = findViewById<Button>(R.id.startStopButton)
        startStopButton.text = "Stop"
    }

    // зупиняємо таймер
    private fun stopTimer() {
        isTimerRunning = false
        timer.cancel()

        // змінюємо текст кнопки на "Start"
        val startStopButton = findViewById<Button>(R.id.startStopButton)
        startStopButton.text = "Start"
    }

    // скидаємо таймер на початок
    private fun resetTimer() {
        timeInSeconds = 0
        updateTimer()

        // змінюємо текст кнопки на "Start"
        val startStopButton = findViewById<Button>(R.id.startStopButton)
        startStopButton.text = "Start"
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
        val seconds = timeInSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}