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
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.appcompat.app.AlertDialog


class MainActivityRegister : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_register)
        val registerButton: Button = findViewById(R.id.registerButton)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val emailEditText: EditText = findViewById(R.id.emailEditText)

        registerButton.setOnClickListener {
            val passwordValue = passwordEditText.text.toString()
            val emailValue = emailEditText.text.toString()

            val intent = when {
                passwordValue == "Лев лох" && emailValue == "1" -> Intent(this, MainActivity::class.java)
                passwordValue == "Лев100%ЛОХ" && emailValue == "2" -> Intent(this, MainActivityUser::class.java)
                passwordValue == "АдріанТоп" && emailValue == "3" -> Intent(this, MainActivityUser1::class.java)
                passwordValue == "Володя красавчік " && emailValue == "4" -> Intent(this, MainActivityUser2::class.java)
                passwordValue == "Лев машина" && emailValue == "5" -> Intent(this, MainActivityUser3::class.java)
                else -> null // Виконується, якщо введений неправильний пароль або електронна пошта
            }

            if (intent != null) {
                startActivity(intent)
            }
        }


        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            // Device supports biometric authentication

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Touch ID Authentication")
                .setDescription("Place your finger on the fingerprint sensor to authenticate")
                .setNegativeButtonText("Cancel")
                .build()

            val biometricPrompt = BiometricPrompt(this, mainExecutor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    // Authentication successful
                    val intent = Intent(this@MainActivityRegister, MainActivity::class.java)
                    startActivity(intent)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    // Authentication error
                    // Handle error accordingly
                }
            })

            biometricPrompt.authenticate(promptInfo)
        } else {
            // Device does not support biometric authentication
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Biometric Authentication")
                .setMessage("фу цей телефон навіть touch id викинь його і купи iPhone") //Your device does not support biometric authentication. Continue with another authentication method?
                .setPositiveButton("OK") { dialog, _ ->
                    // Handle OK button click
                    // You can navigate to another activity or perform other actions here
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    // Handle Cancel button click
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()
        }

        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            // Пристрій підтримує біометричну аутентифікацію

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Face ID Authentication")
                .setDescription("Place your face in front of the front camera to authenticate")
                .setNegativeButtonText("Cancel")
                .build()

            val biometricPrompt = BiometricPrompt(this, mainExecutor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    // Аутентифікація пройшла успішно
                    val intent = Intent(this@MainActivityRegister, MainActivityUser::class.java)
                    startActivity(intent)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    // Виникла помилка аутентифікації
                    // Обробте помилку відповідним чином
                }
            })

            biometricPrompt.authenticate(promptInfo)
        } else {
            // Пристрій не підтримує біометричну аутентифікацію
            // Ви можете використовувати інший метод аутентифікації або повідомити користувача про відсутність підтримки
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("No Biometric Support")
                .setMessage("Your device does not support biometric authentication.")
                .setPositiveButton("OK") { _, _ ->
                    // Обробка події натискання кнопки OK
                }
                .create()
            alertDialog.show()
        }
    }
}