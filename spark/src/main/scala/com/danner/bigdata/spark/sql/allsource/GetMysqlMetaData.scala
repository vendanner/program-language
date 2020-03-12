package com.danner.bigdata.spark.sql.allsource

import java.sql._

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer

/**
  * 获取元数据
  */
object GetMysqlMetaData {
    var logger = LoggerFactory.getLogger(GetMysqlMetaData.getClass.getSimpleName)

    /**
      * 获取MySQL配置表元数据信息
      * @param spark
      * @return
      */
    def getMysqlMeta(spark:SparkSession) = {
        var connect: Connection = null
        var statement: Statement = null
        var result: ResultSet = null
        val dataSources = new ListBuffer[DataSource]
        val mysql_table = "ext_source"

        try {
            Class.forName("com.mysql.jdbc.Driver")
            connect = DriverManager.getConnection("jdbc:mysql://ip:3306/test",
                "","")
            statement = connect.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
            result = statement.executeQuery("select * from " + mysql_table + " where enable = 0 ")

            while (result.next()) {
                val datasourcetype = result.getString("datasourcetype")
                val url = result.getString("url")
                val sourcetable = result.getString("sourcetable")
                val targettable = result.getString("targettable")
                val username = result.getString("username")
                val password = result.getString("password")
                val enable = result.getInt("enable")

                dataSources += DataSource(datasourcetype.toInt, url, sourcetable, targettable, username, password, enable)

            }

        } catch {
            case e: SQLException => e.printStackTrace()
            case e1: Exception => e1.printStackTrace()
            case _ : IllegalArgumentException => println("Illegal Argument!")

        } finally {
            connect.close()
            statement.close()
            result.close()
        }
        dataSources
    }

    /**
      * 注册临时表
      * @param spark
      * @return
      */
    def parseMysqlMetaData(spark:SparkSession):Unit = {

        val dataSources = getMysqlMeta(spark)
        for (dataSource <- dataSources) {
            dataSource.datasourcetype match {
                //mysql
                case 1 =>
                    logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>load mysql : " +
                            dataSource.sourcetable + "to " + dataSource.targettable)
                    spark.read.format("jdbc")
                            .option("url", dataSource.url)
                            .option("dbtable", dataSource.sourcetable)
                            .option("user", dataSource.username)
                            .option("password", dataSource.password)
                            .option("driver","com.mysql.jdbc.Driver")
                            .load().createOrReplaceTempView(dataSource.targettable)

                //hive
                case 2 =>
                    spark.table(dataSource.sourcetable).createOrReplaceTempView(dataSource.targettable)

                //sqlserver
                case 3 =>
                    logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>load sqlserver : "
                            + dataSource.sourcetable + "to " + dataSource.targettable)

                    spark.read.format("jdbc")
                            .option("url", dataSource.url)
                            .option("dbtable", dataSource.sourcetable)
                            .option("user", dataSource.username)
                            .option("password", dataSource.password)
                            .option("driver","com.microsoft.sqlserver.jdbc.SQLServerDriver")
                            .load().createOrReplaceTempView(dataSource.targettable)

                case _ =>
            }
        }
    }
}

