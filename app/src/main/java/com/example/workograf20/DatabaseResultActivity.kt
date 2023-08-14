package com.example.workograf20

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.sql.DriverManager
import java.sql.SQLException

class DatabaseResultActivity : AppCompatActivity() {

    private lateinit var dbReader: DatabaselsIsRead

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_result)

        dbReader = DatabaselsIsRead()

        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)

        GlobalScope.launch {
            val result = dbReader.databaseIsRead()
            withContext(Dispatchers.Main) {
                addDataToTable(result, tableLayout)
            }
        }
    }

    private fun addDataToTable(data: List<String>, tableLayout: TableLayout) {
        val tableRowParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )

        for (line in data) {
            val dataRow = line.split(", ").toTypedArray()
            val row = TableRow(this)
            row.layoutParams = tableRowParams

            for (cell in dataRow) {
                val textView = TextView(this)
                textView.text = cell
                textView.gravity = Gravity.CENTER
                textView.setBackgroundResource(R.drawable.textview_border)
                textView.setOnClickListener {
                    copyTextToClipboard(cell)
                }
                row.addView(textView)
            }

            tableLayout.addView(row)
        }
    }

    private fun copyTextToClipboard(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Database Result", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    // Функція обробки натискання кнопки "Save to CSV"
    fun onSaveToCsvButtonClick(view: View) {
        val data = mutableListOf<List<String>>()
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)

        // Збереження даних з таблиці у списку
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as TableRow
            val rowData = mutableListOf<String>()
            for (j in 0 until row.childCount) {
                val cell = row.getChildAt(j) as TextView
                rowData.add(cell.text.toString())
            }
            data.add(rowData)
        }

        // Збереження списку даних у CSV-файл
        val fileName = "database_results.csv"
        val csvContent = data.joinToString("\n") { it.joinToString(",") }
        saveToCsvFile(fileName, csvContent)

        // Відображення повідомлення про успішне збереження
        Toast.makeText(this, "Data saved to $fileName", Toast.LENGTH_SHORT).show()
    }

    // Функція для збереження даних у CSV-файл
    private fun saveToCsvFile(fileName: String, content: String) {
        try {
            val downloadDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val csvFile = File(downloadDir, fileName)
            csvFile.writeText(content)
            MediaScannerConnection.scanFile(this, arrayOf(csvFile.toString()), null, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
   /* private fun formatResult(result: List<String>): String {
        // Форматування результатів
        return result.joinToString("\n")
    }*/
