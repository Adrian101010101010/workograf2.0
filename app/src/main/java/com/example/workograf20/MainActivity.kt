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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var openDrawerButton: Button
    private lateinit var navigationView: NavigationView

    private var isTimerRunning = false
    private lateinit var timer: CountDownTimer
    private var timeInSeconds = 0L

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        openDrawerButton = findViewById(R.id.openNavigationView)
        openDrawerButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_item1 -> {
                    showExitConfirmationDialog()
                    true
                }
                R.id.nav_item2 -> {
                    val intent = Intent(this, LocationMap::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }


        // Відновлення стану таймера при повторному вході в програму
        if (savedInstanceState != null) {
            isTimerRunning = savedInstanceState.getBoolean("isTimerRunning", false)
            timeInSeconds = savedInstanceState.getLong("timeInSeconds", 0)
        }

        val timerTextView = findViewById<TextView>(R.id.timerTextView)
        val resetButton = findViewById<Button>(R.id.resetButton)
        resetButton.setOnClickListener {
            resetTimer()
        }

        val startStopButton = findViewById<Button>(R.id.startStopButton)
        startStopButton.setOnClickListener {
            if (isTimerRunning) {
                startTimer()
            }
        }

        timerTextView.text = formatTime(timeInSeconds)
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

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isTimerRunning", isTimerRunning)
        outState.putLong("timeInSeconds", timeInSeconds)
    }

    override fun onResume() {
        super.onResume()
        updateTimer()
    }

    private fun startTimer() {
        isTimerRunning = true

        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeInSeconds++
                updateTimer()
            }

            override fun onFinish() {}
        }

        timer.start()

        val startStopButton = findViewById<Button>(R.id.startStopButton)
        startStopButton.text = "Stop"
    }

    private fun resetTimer() {
        timeInSeconds = 0
        isTimerRunning = false
        timer.cancel()
        updateTimer()

        val startStopButton = findViewById<Button>(R.id.startStopButton)
        startStopButton.text = "Start"
    }

    private fun updateTimer() {
        val timerTextView = findViewById<TextView>(R.id.timerTextView)
        timerTextView.text = formatTime(timeInSeconds)
    }

    private fun formatTime(timeInSeconds: Long): String {
        val hours = timeInSeconds / 3600
        val minutes = (timeInSeconds % 3600) / 60
        val seconds = timeInSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}