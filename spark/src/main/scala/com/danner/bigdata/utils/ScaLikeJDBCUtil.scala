package com.danner.bigdata.utils

import com.typesafe.config.ConfigFactory
import scalikejdbc.{ConnectionPool, ConnectionPoolSettings, DB, DBConnection, NamedDB, SQL, WrappedResultSet}

object ScaLikeJDBCUtil {

    val connectionPoolName = "scalike"

    // 读取 application.conf 文件
    val config = ConfigFactory.load()
    val url = config.getString("db.default.url")
    val user = config.getString("db.default.user")
    val password = config.getString("db.default.password")
    val driver = config.getString("db.default.driver")

    val settings = ConnectionPoolSettings(
        initialSize = 5,
        maxSize = 20,
        connectionTimeoutMillis = 3000L,
        validationQuery = "select 1 from dual")

    Class.forName(driver)
    ConnectionPool.add(connectionPoolName,url,user,password,settings)

    def getConnection : DBConnection ={
        NamedDB(connectionPoolName)
    }


    /**
      * 查询 sql
      * @param sql
      * @return
      */
    def query[A](sql:String,function: WrappedResultSet => A) ={
        NamedDB(connectionPoolName).readOnly({
            implicit session =>{
                SQL(sql).map(function).list().apply()
            }
        } )
    }
    /**
      * 执行 sql
      * @param sql
      * @return
      */
    def localTx(sql:String)={
        NamedDB(connectionPoolName).localTx({
            implicit session=> {
                SQL(sql).update().apply()
            }
        })
    }

    def main(args: Array[String]): Unit = {
        val sql = "select * from ssc_wc"
    }
}
