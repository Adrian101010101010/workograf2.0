package com.example.workograf20
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.hardware.fingerprint.FingerprintManager
import android.os.CancellationSignal
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color.BLACK
import android.graphics.Color.RED
import android.os.Build
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager


@Suppress("DEPRECATION")
class MainActivityRegister : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_register)

        val registerButton: Button = findViewById(R.id.registerButton)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val emailEditText: EditText = findViewById(R.id.emailEditText)

        val biometricManager = BiometricManager.from(this)

        // Виклик методу для налаштування біометричної аутентифікації
        setupBiometricAuthentication()

        // Налаштовуємо фокус та обробник для emailEditText
        emailEditText.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                // Переключаємо фокус на passwordEditText
                passwordEditText.requestFocus()
                true // Повертаємо true, щоб вказати, що обробили подію
            } else {
                false // Повертаємо false, якщо подія не оброблена
            }
        }

        // Тепер використовуємо інший обробник для passwordEditText
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Викликати клік на registerButton
                registerButton.performClick()
                true // Повертаємо true, щоб вказати, що обробили подію
            } else {
                false // Повертаємо false, якщо подія не оброблена
            }
        }


        registerButton.setOnClickListener {
            val passwordValue = passwordEditText.text.toString()
            val emailValue = emailEditText.text.toString()

            val selectedPageClass = when {
                passwordValue == "1" && emailValue == "1" -> MainActivity::class.java.name
                passwordValue == "2" && emailValue == "2" -> MainActivityUser::class.java.name
                passwordValue == "3" && emailValue == "3" -> MainActivityUser1::class.java.name
                passwordValue == "4 " && emailValue == "4" -> MainActivityUser2::class.java.name
                passwordValue == "5" && emailValue == "5" -> MainActivityUser3::class.java.name
                else -> null
            }

            if (selectedPageClass != null) {
                val intent = Intent(this, Class.forName(selectedPageClass))
                startActivity(intent)
            } else {
                passwordEditText.setTextColor(RED)
                emailEditText.setTextColor(RED)
                // ...
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setupBiometricAuthentication() {
        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Touch ID Authentication")
                .setDescription("Place your finger on the fingerprint sensor to authenticate")
                .setNegativeButtonText("Cancel")
                .build()

            val biometricPrompt = BiometricPrompt(
                this,
                mainExecutor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        val savedSelectedPageClass =
                            sharedPreferences.getString("selectedPage", null)
                        savedSelectedPageClass?.let {
                            startActivity(
                                Intent(
                                    this@MainActivityRegister,
                                    Class.forName(savedSelectedPageClass)
                                )
                            )
                        }
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        // Handle authentication error
                    }
                })

            biometricPrompt.authenticate(promptInfo)
        } else {
            showNoBiometricSupportDialog()
        }
    }

    private fun showNoBiometricSupportDialog() {
        if (!isFinishing) {
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("No Biometric Support")
                .setMessage("Your device does not support biometric authentication.")
                .setPositiveButton("OK") { _, _ -> /* Handle OK button click */ }
                .create()

            alertDialog.show()
        }
    }
}