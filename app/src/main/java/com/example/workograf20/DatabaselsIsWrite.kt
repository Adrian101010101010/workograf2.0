package com.example.workograf20


import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Timestamp
import java.util.Calendar
import java.util.TimeZone
import java.time.Duration
import java.time.Instant

class DatabaseIsWrite {
    @RequiresApi(Build.VERSION_CODES.O)
    fun databaseIsWrite(
        id: Int,
        timeInSeconds: Long,
        period: Long,
        today: Long,
        week: Long,
        month: Long
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val user = "coldvl"
            val password = "qwertyP1"
            val host = "hv305868-001.ca.clouddb.ovh.net"
            val database = "workograph-db-test"
            val port = 35967

            val url = "jdbc:mysql://$host:$port/$database"
            val connection = DriverManager.getConnection(url, user, password)

            try {
                connection.autoCommit = false

                val name = "Mary Jane"

                val timeZone = TimeZone.getDefault()
                val startedTime = Calendar.getInstance(timeZone)
                val startedTimestamp = Timestamp(startedTime.timeInMillis)

                val totalDuration = Duration.ofSeconds(timeInSeconds)
                val totalSeconds = totalDuration.seconds

                val totalHours = totalSeconds / 3600
                val totalMinutes = (totalSeconds % 3600) / 60
                val totalSecondsRemaining = totalSeconds % 60
                val totalTimeFormatted =
                    String.format("%02d:%02d:%02d", totalHours, totalMinutes, totalSecondsRemaining)

                val periodHours = period / 3600
                val periodMinutes = (period % 3600) / 60
                val periodSecondsRemaining = period % 60
                val periodTimeFormatted =
                    String.format("%02d:%02d:%02d", periodHours, periodMinutes, periodSecondsRemaining)

                val todayHours = today / 3600
                val todayMinutes = (today % 3600) / 60
                val todaySecondsRemaining = today % 60
                val todayTimeFormatted =
                    String.format("%02d:%02d:%02d", todayHours, todayMinutes, todaySecondsRemaining)

                val weekHours = week / 3600
                val weekMinutes = (week % 3600) / 60
                val weekSecondsRemaining = week % 60
                val weekTimeFormatted =
                    String.format("%02d:%02d:%02d", weekHours, weekMinutes, weekSecondsRemaining)

                val monthHours = month / 3600
                val monthMinutes = (month % 3600) / 60
                val monthSecondsRemaining = month % 60
                val monthTimeFormatted =
                    String.format("%02d:%02d:%02d", monthHours, monthMinutes, monthSecondsRemaining)

                val query = """
            INSERT INTO `employees` (`id`, `name`, `startedTime`, `totalTime`, `period`, `today`, `week`, `month`)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
            `name` = VALUES(`name`),
            `startedTime` = VALUES(`startedTime`),
            `totalTime` = ADDTIME(`totalTime`, VALUES(`totalTime`)),
            `period` = VALUES(`period`),
            `today` =  ADDTIME(`today`, VALUES(`today`)),
            `week` = ADDTIME(`week`, VALUES(`week`)),
            `month` = ADDTIME(`month`, VALUES(`month`))
            """
                val statement = connection.prepareStatement(query)
                statement.setInt(1, id)
                statement.setString(2, name)
                statement.setTimestamp(3, startedTimestamp)
                statement.setString(4, totalTimeFormatted)
                statement.setString(5, periodTimeFormatted)
                statement.setString(6, todayTimeFormatted)
                statement.setString(7, weekTimeFormatted)
                statement.setString(8, monthTimeFormatted)

                statement.executeUpdate()

                connection.commit()
            } catch (e: SQLException) {
                e.printStackTrace()
                connection.rollback()
            } finally {
                connection.close()
            }
        }
    }
}
/*fun main() {
    val dbWriter = DatabaseIsWrite()
    dbWriter.databaseIsWrite()
}*/