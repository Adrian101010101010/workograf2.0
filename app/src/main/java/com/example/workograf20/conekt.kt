package com.example.workograf20
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

fun main() {
    val user = "coldvl"  // Username for the database
    val password = "qwertyP1"  // Password for the database
    val host = "hv305868-001.ca.clouddb.ovh.net"  // Hostname or IP address of the database server
    val database = "workograph-db-test"  // Name of the database
    val port = 35967  // Port used for the connection

    try {
        // Load the JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver")

        // Set up the connection URL
        val url = "jdbc:mysql://$host:$port/$database"

        // Establish the connection
        val connection: Connection = DriverManager.getConnection(url, user, password)

        // If the connection is successful, print a success message
        println("Connection to the database successful!")

        // Close the connection
        connection.close()
    } catch (e: ClassNotFoundException) {
        // If the JDBC driver is not found
        e.printStackTrace()
    } catch (e: SQLException) {
        // If there's an error in the connection
        e.printStackTrace()
    }
}