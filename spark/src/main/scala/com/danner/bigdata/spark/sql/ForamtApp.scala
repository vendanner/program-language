package com.danner.bigdata.spark.sql

import com.danner.bigdata.utils.ScaLikeJDBCUtil
import com.typesafe.config.ConfigFactory
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row,DataFrame, Dataset, SaveMode, SparkSession,functions}
import scalikejdbc.WrappedResultSet
import org.apache.phoenix.spark._

/**
  * 读写 txt、json、csv、hive、jdbc 数据
  */
object ForamtApp {

    /**
      * textfile : TextFileFormat
      *             TextOptions
      *  读数据：DataFrameReader
      *  写数据：DataFrameWriter
      * @param spark
      */
    def text(spark: SparkSession) = {

        import spark.implicits._

        val df = spark.read.option("","")
          .format("text")
          .load("input/sql/people.txt")

        //  textFile 是在 load file 之后，select("value").as[String](sparkSession.implicits.newStringEncoder)
        val value: Dataset[String] = spark.read.textFile("input/sql/people.txt")
        val ds = value.map(x => {
            val splits = x.split(",")
            val name = splits(0)
            val age = splits(1).trim
            (name , age)
        })
//        ds.toDF("value").write.format("text").mode(SaveMode.Overwrite).save("out")
        // ds 转化成 df 合并成一列后存入 text
        val allClumnName: String = ds.toDF().columns.mkString(",")
        val result = ds.toDF().selectExpr(s"concat_ws(',',$allClumnName) as allclumn")
        result.write.format("text").mode(SaveMode.Overwrite).save("out")

//        ds.write.format("text").mode(SaveMode.Overwrite).save("out")
//        val rdd = df.rdd.map(x => {
//            val word = x.getString(0)
//            val splits = word.split(",")
//            val name = splits(0)
//            val age = splits(1).trim.toInt
//            (name, age)
//        })

    }

    /**
      * JsonDataSource JSONOptions
      *
      * @param spark
      */
    def json(spark: SparkSession):Unit = {
        import spark.implicits._

        val df: DataFrame = spark.read.format("json").load("input/sql/people.json")
        df.printSchema()
        df.show()
        val result = df.select("name").filter('age > 20)
        result.show()
        result.write.format("json").mode(SaveMode.Overwrite).save("out")

    }

    /**
      * CSVDataSource CSVOptions
      * @param spark
      */
    def csv(spark: SparkSession) = {
        import spark.implicits._

        val df = spark.read.format("csv").option("header","true").
                option("sep",";").load("input/sql/people.csv")
        df.show()

        val result = df.select("name","job").filter('age > 30)
        result.show()
        result.write.format("csv").mode(SaveMode.Overwrite).save("out")
    }

    /**
      * 配置 spark-hive_xxx 依赖
      * hive :
      *     将 HDFS 下 core-site.xml.bak.ali.bak.bak 和
      *     hive下 hive-site.xml.bak 复制到 res
      *     hive下 hive-site.xml.bak 复制到 res
      * @param spark
      */
    def hive(spark: SparkSession):Unit = {
        import spark.implicits._

        spark.sql("use danner_db")
        spark.sql("show tables").show()
        spark.table("platform_stat").show()
    }


    /**
      * jdbc
      *     JDBCOptions
      *
      * JdbcRelationProvider
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

        val df = spark.read.format("jdbc")
                .option("url", url)
                .option("dbtable", srcTable)
                .option("user", user)
                .option("password", password)
                .load()
        df.count
        df.sortWithinPartitions()
        df.repartition().sortWithinPartitions()

        df.show()
    }

    def main(args: Array[String]): Unit = {
        // 配置访问用户
//        System.setProperty("HADOOP_USER_NAME","hadoop")
        // Hive 操作要设置 enableHiveSupport
        val spark = SparkSession.builder().appName("ForamtApp").master("local[2]")
//                .config("hive.metastore.uris", "thrift://192.168.22.147:9083")
                .enableHiveSupport()
                .getOrCreate()

        json(spark)
//        json(spark)
//        csv(spark)
//        hive(spark)

//        jdbc(spark)

//        val sql = "select * from word_int"
//        val function = (rs: WrappedResultSet) => {
//            val word = rs.string("word")
//            val traffic = rs.long("traffic")
//            (word,traffic.toString)
//        }
//        val map: Map[String, String] = ScaLikeJDBCUtil.query(sql,function).toMap
//
//        spark.sparkContext.parallelize(map.toList).foreach(println)



        spark.close()

    }
}

