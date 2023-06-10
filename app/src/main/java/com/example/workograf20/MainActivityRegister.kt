package com.example.workograf20
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivityRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_register)
        val registerButton: Button = findViewById(R.id.registerButton)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)

        registerButton.setOnClickListener {
            val passwordValue = passwordEditText.text.toString()
            val intent = if (passwordValue == "Лев лох") {
                Intent(this, MainActivity::class.java)
            } else if (passwordValue == "Лев100%ЛОХ") {
                Intent(this,MainActivityUser::class.java)
            } else {
                // Виконується якщо введена інша цифра або текст
                // Можна додати обробку помилки або інше поведінку
                null
            }

            if (intent != null) {
                startActivity(intent)
            }
        }
    }
}