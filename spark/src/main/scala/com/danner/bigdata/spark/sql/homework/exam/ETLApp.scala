package com.danner.bigdata.spark.sql.homework.exam

import java.io.RandomAccessFile

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.lionsoul.ip2region.{DbConfig, DbSearcher}
import org.apache.spark.sql.functions.udf

/**
  * 数据清洗
  */
object ETLApp {

    def getDbBinStr(path:String)={

        val raf = new RandomAccessFile(path, "r")
        val dbBinStr = new Array[Byte](raf.length.asInstanceOf[Int])
        raf.seek(0L)
        raf.readFully(dbBinStr, 0, dbBinStr.length)
        dbBinStr
    }
    def main(args: Array[String]): Unit = {
        System.setProperty("HADOOP_USER_NAME","hadoop")
        val spark = SparkSession.builder().master("local[2]").appName("ETLApp")
                .enableHiveSupport()
                .getOrCreate()

        // hdfs 相对路径 (/user/hadoop/)
        val inPath = "input/data-test.json"
        val ipDbPath = "input/ip2region.db"
        val TABLE = "ad_records"

        val df = spark.read.format("json").load(inPath)

        val dbBinStr = getDbBinStr(ipDbPath)
//        val searcher = new DbSearcher(config,)
        val bc_dbBinStr = spark.sparkContext.broadcast(dbBinStr)

        // 中国|0|浙江省|杭州市|电信
//        println(searcher.memorySearch("115.193.177.71").getRegion)

        // 处理
        import spark.implicits._

        val province = (ip:String) => {
            val config = new DbConfig()
            val dbBinStr_val = bc_dbBinStr.value
            val searcher_val = new DbSearcher(config,dbBinStr_val)
            val region = searcher_val.memorySearch(ip).getRegion
            if (region != null && region.split("\\|").length == 5) {
                val province = region.split("\\|")(2)
                province
            } else {
               null
            }
        }
        val city = (ip:String) => {
            val config = new DbConfig()
            val dbBinStr_val = bc_dbBinStr.value
            val searcher_val = new DbSearcher(config,dbBinStr_val)
            val region = searcher_val.memorySearch(ip).getRegion
            if (region != null && region.split("\\|").length == 5) {
                val city = region.split("\\|")(3)
                city
            } else {
                null
            }
        }

        val getprovince: UserDefinedFunction = udf(province)
        val getcity = udf(city)
        val result = df.withColumn("province",getprovince(df("ip")))
                        .withColumn("city",getcity(df("ip")))
        result.show()

        // 保存
        spark.sql("use danner_db")
        spark.sql("show tables").show()
        result.write.mode(SaveMode.Overwrite).saveAsTable(TABLE)

        spark.close()
    }
}
