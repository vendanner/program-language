package com.danner.bigdata.spark.sql.allsource

import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory

/**
  * 多数据库数据源整合：
  * 数据在不同的数据库不同表，需要集中到 hive 表；寻常做法是spark sql 接入数据源，sql 处理后存入 hive
  * 但上面的做法需要每次都写代码繁琐，下面提供的是一个小工具
  * 1、ext_source 表：存储需要导入的表
  * 2、sqlFile：接入数据源处理后要存储的表，是sql 语句
  * 3、spark sql 代码：根据 ext_source 载入数据源，执行 sqlFile 导出数据到表
  *
  * 其实就是以参数的形式调用spark sql 代码，省得每次都要改写代码，麻烦
  */
object AllSql  extends Serializable{

    def main(args: Array[String]): Unit = {

        var logger = LoggerFactory.getLogger(this.getClass.getSimpleName)
        val sqlFile = args(0)
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>初始化sparkContext...")

//        val conf = new SparkConf()
//                .setAppName("sync mysql to hive")
//                .set("hive.exec.max.dynamic.partitions", "5000")
//                .set("spark.sql.parquet.compression.codec", "lzo")
//                .set("spark.sql.shuffle.partitions","100")
//                .set("spark.sql.crossJoin.enabled","true")

//        val sc = new SparkContext(conf)
//        val hiveContext = new HiveContext(sc)
//        hiveContext.setConf("spark.default.parallelism","500")
//        hiveContext.setConf("hive.exec.dynamic.partition.mode", "nonstrict")

        val spark = SparkSession.builder().enableHiveSupport()
                .config("hive.metastore.uris", "thrift://192.168.22.147:9083")
                .config("hive.exec.max.dynamic.partitions", "5000")
                .config("spark.sql.parquet.compression.codec", "lzo")
                .config("spark.sql.shuffle.partitions", "100")
                .config("spark.sql.crossJoin.enabled", "true")
                .config("spark.default.parallelism", "500")
                .config("hive.exec.dynamic.partition.mode", "nonstrict")
                .appName("sync mysql to hive").getOrCreate()

        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>注册临时表...")
        GetMysqlMetaData.parseMysqlMetaData(spark)
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>注册临时表成功！")

        spark.sql("show tables").show(false)

        spark.sql(parseSqlFile(sqlFile)).show(false)


        spark.stop()

        import scala.io.Source
        def parseSqlFile(sqlFile: String):String = {
            val source = Source.fromFile(sqlFile, "UTF-8")

            //将文件内容读成字符串
            val lines = source.mkString
            source.close
            print(s"sql: $lines")
            lines
        }
    }
}
/**
  * * metadata of external data source
  * * @param datasourcetype
  * * @param url
  * * @param sourcetable
  * * @param targettable
  * * @param username
  * * @param password
  * * @param enable
  *
  */
case class DataSource(datasourcetype: Int,
                      url: String,
                      sourcetable: String,
                      targettable: String,
                      username: String,
                      password: String,
                      enable: Int) extends Serializable
