package com.example.workograf20

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.os.Handler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
//LocationMap
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

@Suppress("DEPRECATION")
class MainActivityUser : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var openDrawerButton: Button
    private lateinit var navigationView: NavigationView
    private lateinit var connectivityReceiver: BroadcastReceiver
    private lateinit var alertDialog: AlertDialog

    private var isTimerRunning = false
    private lateinit var timer: CountDownTimer
    private var timeInSeconds = 0L
    private lateinit var resetButton: Button


    private val handler = Handler()
    private val resetDelay = 5000L // Затримка скидання таймера в мілісекундах
    private val dbWriter = DatabaseIsWrite()
//LocationMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private lateinit var textView: TextView



    companion object {
        private const val TARGET_LATITUDE = 49.834870 // Задайте цільову широту
        private const val TARGET_LONGITUDE = 24.021730 // Задайте цільову довготу

        private const val PERMISSION_REQUEST_CODE = 123
    }

    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_user)

        //LocationMap
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        textView = findViewById(R.id.textView)

        // Check location permission
        if (hasLocationPermission()) {
            requestLocationUpdates()
        } else {
            requestLocationPermission()
        }





        // Отримайте останнє значення таймера з попередньої активності (якщо таке є)
        timeInSeconds = intent.getLongExtra("timeInSeconds", 0)

        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            intent.putExtra("timeInSeconds", timeInSeconds)
            startActivity(intent)
        }

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

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val buttonTextColor = ColorStateList.valueOf(Color.WHITE)
       // val buttonTextColor = ContextCompat.getColorStateList(this, R.drawable.left_menu_text)
        navigationView.itemTextColor = buttonTextColor

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
                // Зупиняємо таймер
                resetTimer()
            } else {
                // Запускаємо таймер
                startTimer()
            }
        }

        timerTextView.text = formatTime(timeInSeconds)

        // Create and configure the alert dialog
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("No Internet Connection")//No Internet Connection
            .setMessage("Будь ласка підключілься до інтернету шоб я і надалі мав можливість зливати ваші приватні дані (⓿_⓿)")//Please check your internet connection and try again.
            .setCancelable(false)
        // .setPositiveButton("OK") { _, _ ->
        // Handle OK button click if needed
        // }

        alertDialog = dialogBuilder.create()

        // Initialize the connectivity receiver
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (!isInternetConnected()) {
                    showNoInternetDialog()
                } else {
                    dismissNoInternetDialog()
                }
            }
        }

        // Register the connectivity receiver
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, filter)
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

    @Deprecated("Deprecated in Java")
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
        if (!isTimerRunning) {
            isTimerRunning = true

            timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeInSeconds++
                    updateTimer()
                }

                override fun onFinish() {}
            }

            timer.start()
        } else {
            timer.cancel()
            updateTimer()
        }

        val statisticsButton = findViewById<Button>(R.id.button1)
        statisticsButton.setOnClickListener {
            navigateToStatisticsPage() // Pass the timerValue as a parameter
        }

        val startStopButton = findViewById<Button>(R.id.startStopButton)
        startStopButton.text = "Stop"

        val resetButton = findViewById<Button>(R.id.resetButton)
        resetButton.isEnabled = isTimerRunning
    }

    private fun resetTimer() {
        val resetDelay = 5000L // Затримка скидання таймера в мілісекундах

        val prevTimeInSeconds = timeInSeconds // Зберігаємо попереднє значення часу

        isTimerRunning = false
        timer.cancel()

        handler.postDelayed({
            timeInSeconds = 0
            updateTimer()
            val timerTextView = findViewById<TextView>(R.id.timerTextView)
            timerTextView.text = formatTime(timeInSeconds)

            val startStopButton = findViewById<Button>(R.id.startStopButton)
            startStopButton.text = "Start"

            // Викликаємо метод для запису в базу даних, передаючи попереднє значення часу
            GlobalScope.launch(Dispatchers.IO) {
                dbWriter.databaseIsWrite(6, prevTimeInSeconds, prevTimeInSeconds, prevTimeInSeconds, prevTimeInSeconds, prevTimeInSeconds)
            }
        }, resetDelay)
    }

    private fun updateTimer() {
        val timerTextView = findViewById<TextView>(R.id.timerTextView)
        timerTextView.text = formatTime(timeInSeconds)

        val resetButton = findViewById<Button>(R.id.resetButton)
        resetButton.isEnabled = isTimerRunning
    }

    private fun formatTime(timeInSeconds: Long): String {
        val hours = timeInSeconds / 3600
        val minutes = (timeInSeconds % 3600) / 60
        val seconds = timeInSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
    //усі методи що стосуються сповіщення інтернет
    override fun onDestroy() {
        super.onDestroy()
        // Unregister the connectivity receiver
        unregisterReceiver(connectivityReceiver)
    }

    private fun showNoInternetDialog() {
        if (!alertDialog.isShowing) {
            alertDialog.show()
        }
    }

    private fun dismissNoInternetDialog() {
        if (alertDialog.isShowing) {
            alertDialog.dismiss()
        }
    }

    private fun isInternetConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    private fun navigateToStatisticsPage() {
        val intent = Intent(this, StatisticsActivity::class.java)
        intent.putExtra("timeInSeconds", timeInSeconds)
        startActivity(intent)
    }

//LocationMap
private fun hasLocationPermission(): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LocationMap.PERMISSION_REQUEST_CODE
        )
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    val targetLocation = LatLng(
                        LocationMap.TARGET_LATITUDE,
                        LocationMap.TARGET_LONGITUDE
                    )

                    val distance = calculateDistance(currentLocation, targetLocation)

                    if (distance <= 1000) {
                        textView.text = "On location"
                    } else {
                        textView.text = "Not on location"
                    }
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    // Rest of your code...

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LocationMap.PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates()
            } else {
                Toast.makeText(
                    this,
                    "Location permission denied. Unable to track location.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun calculateDistance(startLocation: LatLng, endLocation: LatLng): Float {
        val startPoint = Location("startPoint").apply {
            latitude = startLocation.latitude
            longitude = startLocation.longitude
        }

        val endPoint = Location("endPoint").apply {
            latitude = endLocation.latitude
            longitude = endLocation.longitude
        }

        return startPoint.distanceTo(endPoint)
    }

}