package com.danner.bigdata.spark.streaming.homework.batch

import com.danner.bigdata.spark.streaming.homework.BroadcastAlert
import com.danner.bigdata.utils.ScaLikeJDBCUtil
import com.typesafe.config.ConfigFactory
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * spark sql 批处理
  * 1、加载 hive 表数据进行数据高位词频统计
  * 2、统计结果写到 mysql
  * 3、必须保证整条链路是可重跑
  */
object Analyzer extends Logging{

    def main(args: Array[String]): Unit = {

        val inputPath = "/project/spark/prewarning/log/"
        val hivePath = "/project/spark/prewarning/hive/"
        val day = "20191121"
        val table = "alert_word_cnt"
        // 配置访问用户
        System.setProperty("HADOOP_USER_NAME","hadoop")
        // Hive 操作要设置 enableHiveSupport //
        val spark = SparkSession.builder().appName("ForamtApp").master("local[3]")
                        .config("dfs.client.use.datanode.hostname","true")  // hostname 访问，一般是外网使用
                        .enableHiveSupport()
                        .getOrCreate()


        // hive 中载入数据
        import spark.implicits._
        val dataSet = spark.sql(s"select * from rz_db.prewarn_log where day=${day}").as[BatchLogInfo]

        // 获取预警词
        val bcAlertWord = BroadcastAlert.updateAndGet(spark.sparkContext,null)

        class CustomIterator(iter: Iterator[String]) extends Iterator[String] {
            private val alertWordMap: Map[String, String] = bcAlertWord.value

            def hasNext : Boolean = {
                iter.hasNext
            }
            // 判断是否为 高危词
            def next : String = {
                val word = iter.next()
                val cnt = alertWordMap.getOrElse(word,"-1").toInt
                if(cnt > 0){
                    word
                }else{
                    ""
                }
            }
        }
        // 数据分析,统计warn、error 中的高位词频
        // 不用warn、error 直接操作 msg
        val alertWordCnt = dataSet.rdd.flatMap(_.loginfo.split(" "))
                .mapPartitions(x => new CustomIterator(x))
                // 先去除不是高危的词，减少数据量
                .filter(_.nonEmpty)
                .map((_, 1))
                .reduceByKey(_ + _)

        // 结果存入 mysql
        alertWordCnt.toDF("word","cnt").createOrReplaceTempView("alertWordCnt")
        val dataFrame = spark.sql(s"select word,cnt,${day} as day from alertWordCnt" )
        dataFrame.show()

        // 读取 application.conf 文件
        val config = ConfigFactory.load()
        val url = config.getString("db.default.url")
        val user = config.getString("db.default.user")
        val password = config.getString("db.default.password")
        val driver = config.getString("db.default.driver")
        // 写入之前先删除之前的数据
        ScaLikeJDBCUtil.localTx(s"delete  from rz_db.alert_word_cnt where day=${day};")

        dataFrame.write.format("jdbc")
                .mode(SaveMode.Append)
                // 指定 Driver
                .option("driver",driver)
                .option("url", url)
                .option("dbtable", table)
                .option("user", user)
                .option("password", password)
                .save()


        spark.stop()
    }

    case class BatchLogInfo(hostName:String,serviceName:String,time:String,logType:String,loginfo:String,day:String)
}


