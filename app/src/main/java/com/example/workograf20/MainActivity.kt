package com.example.workograf20

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private var isTimerRunning = false
    private lateinit var timer: CountDownTimer
    private var timeInSeconds = 0L
    private var isTimerReset = true
    private lateinit var drawerLayout: DrawerLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1 = Button(this).apply {
            setBackgroundColor(Color.parseColor("#5caff5"))
            val shape = GradientDrawable()
            shape.cornerRadius = 30 * resources.displayMetrics.density
            background = shape
        }

        //val button1: Button = findViewById(R.id.button1)

       /* button1.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }*/

        val navigationView: NavigationView = findViewById(R.id.navigationView)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        // Встановити слухача для іконки гамбургера, щоб відкрити/закрити Navigation Drawer
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Встановити слухача для натискання пунктів меню Navigation Drawer
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Обробка натискання пунктів меню
            when (menuItem.itemId) {
                R.id.nav_item1 -> {
                    showExitConfirmationDialog()
                    // Дії для пункту меню "Item 1"
                    // Виклик фрагменту або сторінки, пов'язаної з цим пунктом меню
                    true
                }
                R.id.nav_item2 -> {
                    // Дії для пункту меню "Item 2"
                    // Виклик фрагменту або сторінки, пов'язаної з цим пунктом меню
                    true
                }
                // Додайте інші пункти меню за необхідністю
                else -> false
            }
        }

        val button2: Button = findViewById(R.id.button2)


        button2.setOnClickListener {
            val intent = Intent(this, MainActivityRegister::class.java)
            startActivity(intent)
        }

            val timerTextView = findViewById<TextView>(R.id.timerTextView)
            val resetButton = findViewById<Button>(R.id.resetButton)
            val startStopButton = findViewById<Button>(R.id.startStopButton)

            resetButton.setOnClickListener {
                if (isTimerReset) {
                    stopTimer()
                } else {
                    resetTimer()
                }
                isTimerReset = !isTimerReset
            }

            startStopButton.setOnClickListener {
                if (isTimerRunning) {
                    stopTimer()
                } else {
                    startTimer()
                }
            }

            // задаємо початковий вигляд таймера
            timerTextView.text = formatTime(0)
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
            startStopButton.text = "Start"
        }

        // зупиняємо таймер
        private fun stopTimer() {
            isTimerRunning = false
            timer.cancel()

            // змінюємо текст кнопки на "Start"
            val startStopButton = findViewById<Button>(R.id.resetButton)
            startStopButton.text = "Reset"
        }

        // скидаємо таймер на початок
        private fun resetTimer() {
            timeInSeconds = 0
            updateTimer()

            // змінюємо текст кнопки на "Start"
            val startStopButton = findViewById<Button>(R.id.resetButton)
            startStopButton.text = "Stop"
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
            return String.format("%02d:%02d", hours, minutes)
        }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_exit -> {
                showExitConfirmationDialog()
                true
            }
            R.id.nav_item1 -> {
                showExitConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmation")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}