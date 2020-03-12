package com.danner.bigdata.utils

import java.sql.{Connection, DriverManager}

import com.typesafe.config.ConfigFactory

object MySQLUtil {

    val config = ConfigFactory.load()
    val driver = config.getString("db.default.driver")
    val url = config.getString("db.default.url")
    val user = config.getString("db.default.user")
    val password = config.getString("db.default.password")

    def getConnection : Connection ={
        Class.forName(driver)
        DriverManager.getConnection(url,user,password)
    }
    def closeConnection(connection: Connection): Unit ={
        connection.close()
    }

}
