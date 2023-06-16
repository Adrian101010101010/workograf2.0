package com.example.workograf20


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


class StatisticsActivityEmployer : AppCompatActivity() {
    private lateinit var dayTextView1: TextView
    private lateinit var weekTextView1: TextView
    private lateinit var monthTextView1: TextView
    private lateinit var yearTextView1: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_employer)

        dayTextView1 = findViewById<TextView>(R.id.dayTextView1)
        dayTextView1.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java) // Замініть OtherActivity на клас активності, на яку ви хочете перейти
            startActivity(intent)
        }

        weekTextView1 = findViewById<TextView>(R.id.weekTextView1)
        weekTextView1.setOnClickListener {
            val intent = Intent(this, StatisticsActivity1::class.java) // Замініть OtherActivity на клас активності, на яку ви хочете перейти
            startActivity(intent)
        }

        monthTextView1 = findViewById<TextView>(R.id.monthTextView1)
        monthTextView1.setOnClickListener {
            val intent = Intent(this, StatisticsActivity2::class.java) // Замініть OtherActivity на клас активності, на яку ви хочете перейти
            startActivity(intent)
        }

        yearTextView1 = findViewById<TextView>(R.id.yearTextView1)
        yearTextView1.setOnClickListener {
            val intent = Intent(this, StatisticsActivity3::class.java) // Замініть OtherActivity на клас активності, на яку ви хочете перейти
            startActivity(intent)
        }

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            // Закриття поточної активності і повернення до попередньої
            finish()
        }
    }


}