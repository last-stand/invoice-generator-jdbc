package com.learning.invocegenerator.`custom-db-util`

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class CustomH2JDBC {
    companion object {

        fun getDbConnection(): Connection {
            val JDBC_DRIVER = "org.h2.Driver"
            val JDBC_URL = "jdbc:h2:mem:testdb"
            val USER = "sa"
            val PASSWORD = ""

            lateinit var connection: Connection
            try {
                Class.forName(JDBC_DRIVER);
                connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return connection;
        }

        fun executeFetchStatement(sql: String): ResultSet {
            lateinit var statement: Statement
            lateinit var resultSet: ResultSet
            lateinit var connection: Connection
            try {
                connection = getDbConnection()
                statement = connection.createStatement()
                resultSet = statement.executeQuery(sql)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                statement.close()
                connection.close()
            }
            return resultSet
        }

        fun executeUpdateStatement(sql: String): Any {
            lateinit var connection: Connection
            lateinit var statement: Statement
            var result = 0;
            try {
                connection = getDbConnection()
                statement = connection.createStatement()
                result = statement.executeUpdate(sql,  Statement.RETURN_GENERATED_KEYS)
                val keys = statement.getGeneratedKeys()
                keys.last()
                var lastKey = keys.getInt("id")
                return lastKey
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                statement.close()
                connection.close()
            }
            return result
        }
    }

}