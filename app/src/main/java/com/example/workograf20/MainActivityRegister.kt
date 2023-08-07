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

        registerButton.setOnClickListener {
            val passwordValue = passwordEditText.text.toString()
            val emailValue = emailEditText.text.toString()

            val selectedPageClass = when {
                passwordValue == "Лев лох" && emailValue == "1" -> MainActivity::class.java.name
                passwordValue == "Лев100%ЛОХ" && emailValue == "2" -> MainActivityUser::class.java.name
                passwordValue == "АдріанТоп" && emailValue == "3" -> MainActivityUser1::class.java.name
                passwordValue == "Володя красавчік " && emailValue == "4" -> MainActivityUser2::class.java.name
                passwordValue == "Лев машина" && emailValue == "5" -> MainActivityUser3::class.java.name
                else -> null
            }

            if (selectedPageClass != null) {
                val intent = Intent(this, Class.forName(selectedPageClass))
                startActivity(intent)
            } else {
                passwordEditText.setTextColor(RED)
                emailEditText.setTextColor(RED)

               // val intent = Intent(this, MainActivityRegister::class.java)
                //startActivity(intent)
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