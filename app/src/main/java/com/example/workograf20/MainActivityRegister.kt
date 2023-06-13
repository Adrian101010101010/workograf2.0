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
    }
}