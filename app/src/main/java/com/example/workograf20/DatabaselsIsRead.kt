package com.example.workograf20
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.DriverManager
import java.sql.SQLException

class DatabaselsIsRead {
    fun databaseIsRead(): List<String> {
        val user = "coldvl"  // Username for the database
        val password = "qwertyP1"  // Password for the database
        val host = "hv305868-001.ca.clouddb.ovh.net"  // Hostname or IP address of the database server
        val database = "workograph-db-test"  // Name of the database
        val port = 35967  // Port used for the connection

        val url = "jdbc:mysql://$host:$port/$database"
        val connection = DriverManager.getConnection(url, user, password)

        val result: MutableList<String> = mutableListOf()

        try {
            val query = "SELECT * FROM `employees`"
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(query)

            while (resultSet.next()) {
                val id = resultSet.getInt("id")
                val name = resultSet.getString("name")
                val startedTime = resultSet.getString("startedTime")
                val totalTime = resultSet.getString("totalTime")

                val formattedRow = " $id, Name: $name, Started Time: $startedTime, Total Time: $totalTime"
                result.add(formattedRow)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }

        return result
    }
    fun getTotalTimeAndStatsFromDatabase(): Map<String, String> {
        val user = "coldvl"  // Username for the database
        val password = "qwertyP1"  // Password for the database
        val host = "hv305868-001.ca.clouddb.ovh.net"  // Hostname or IP address of the database server
        val database = "workograph-db-test"  // Name of the database
        val port = 35967  // Port used for the connection

        val url = "jdbc:mysql://$host:$port/$database"
        val connection = DriverManager.getConnection(url, user, password)

        val result: MutableMap<String, String> = mutableMapOf()

        try {
            val query = "SELECT totalTime, period, today, week, month FROM `employees` WHERE id = 6"
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(query)

            if (resultSet.next()) {
                result["totalTime"] = resultSet.getString("totalTime")
                result["period"] = resultSet.getString("period")
                result["today"] = resultSet.getString("today")
                result["week"] = resultSet.getString("week")
                result["month"] = resultSet.getString("month")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }

        return result
    }

    fun getTotalTimeAndStatsFromDatabaseEmployer(): Map<String, String> {
        val user = "coldvl"  // Username for the database
        val password = "qwertyP1"  // Password for the database
        val host = "hv305868-001.ca.clouddb.ovh.net"  // Hostname or IP address of the database server
        val database = "workograph-db-test"  // Name of the database
        val port = 35967  // Port used for the connection

        val url = "jdbc:mysql://$host:$port/$database"
        val connection = DriverManager.getConnection(url, user, password)

        val result: MutableMap<String, String> = mutableMapOf()

        try {
            val query = "SELECT totalTime, period, today, week, month FROM `employees` WHERE id = 2"
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(query)

            if (resultSet.next()) {
                result["totalTime"] = resultSet.getString("totalTime")
                result["period"] = resultSet.getString("period")
                result["today"] = resultSet.getString("today")
                result["week"] = resultSet.getString("week")
                result["month"] = resultSet.getString("month")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection.close()
        }

        return result
    }
}

/*fun main() {
    val dbReader = DatabaselsIsRead()
    dbReader.databaseIsRead()
}*/