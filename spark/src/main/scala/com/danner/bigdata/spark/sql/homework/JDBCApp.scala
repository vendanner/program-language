package com.danner.bigdata.spark.sql.homework

import com.danner.bigdata.spark.sql.ForamtApp.jdbc
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * 通过jdbc 访问数据库
  * spark-submit --class com.danner.bigdata.spark.sql.homework.JDBCApp --master local[2] --name jdbcApp
  * --jars /home/hadoop/lib/dependency/typesafe.config-1.2.1.jar,
  * /home/hadoop/lib/dependency/mysql-connector-java-5.1.45-bin.jar
  * spark-flink-1.0.jar
  */
object JDBCApp {
    /**
      * jdbc
      *     JDBCOptions
      * @param spark
      */
    def jdbc(spark: SparkSession) = {
        import spark.implicits._

        // scalikejdbc 依赖中包含
        // 读取 application.conf 文件
        val config = ConfigFactory.load()
        val url = config.getString("db.default.url")
        val user = config.getString("db.default.user")
        val password = config.getString("db.default.password")
        val srcTable = config.getString("db.default.srcTable")
        val targetTable = config.getString("db.default.targetTable")
        val driver = config.getString("db.default.driver")

        val df = spark.read.format("jdbc")
                // 指定 driver
                .option("driver",driver)
                .option("url", url)
                .option("dbtable", srcTable)
                .option("user", user)
                .option("password", password)
                .load()

        df.show()

        // 若没有表会自动创建
        df.write.format("jdbc")
                .mode(SaveMode.Append)
                // 指定 Driver
                .option("driver",driver)
                .option("url", url)
                .option("dbtable", targetTable)
                .option("user", user)
                .option("password", password)
                .save()
    }
    def main(args: Array[String]): Unit = {

        // Hive 操作要设置 enableHiveSupport
        val spark = SparkSession.builder().getOrCreate()

        jdbc(spark)

        spark.close()

    }
}
