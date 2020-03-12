package com.danner.bigdata.spark.sql.homework.exam

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql._
import org.apache.spark.sql.functions._

object StatApp {

    def saveToMySQL(spark: SparkSession, df: DataFrame) = {
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

        // 若没有表会自动创建
        df.write.format("jdbc")
                .mode(SaveMode.Overwrite)
                // 指定 Driver
                .option("driver",driver)
                .option("url", url)
                .option("dbtable", targetTable)
                .option("user", user)
                .option("password", password)
                .option("createTableOptions","CREATE TABLE t (name string) ENGINE=InnoDB DEFAULT CHARSET=utf8")
                .save()

    }

    def main(args: Array[String]): Unit = {
        System.setProperty("HADOOP_USER_NAME","hadoop")
        val spark = SparkSession.builder().master("local[2]").appName("StatApp")
                .enableHiveSupport()
                .getOrCreate()

        val TABLE = "ad_records"

        import spark.implicits._

        spark.sql("use danner_db")
        val df = spark.table(TABLE)
//        df.printSchema()
//        df.show()
       df.createOrReplaceTempView("ad_info")
//        spark.sql("select * from ad_info").show()
        val sql =
            """
              |select province,city,adplatformproviderid,requestmode,processnode,
              |iseffective,isbilling,isbid,iswin,adorderid from ad_info
            """.stripMargin
        val info = spark.sql(sql).as[Info]
        spark.sql(sql).createOrReplaceTempView("SYS_AD")

        val statSql =
        """
          |select AD.* ,
          |ROUND(AD.adbidseccesscounts/AD.adbidcounts * 100, 2) bidrate,
          |ROUND(AD.adclickcounts/AD.addispalycounts * 100, 2) clickrate
          |from
          |(
          |select
          |province,
          |city,
          |sum (case when requestmode=1 and processnode>=1 then 1 else 0 end) requestmodecounts,
          |sum (case when requestmode=1 and processnode>=2 then 1 else 0 end) processnodecounts,
          |sum (case when requestmode=1 and processnode=3 then 1 else 0 end) adrequestcounts,
          |sum (case when adplatformproviderid>=100000 and iseffective=1
          |     and isbilling=1 and isbid=1 and adorderid!=0 then 1 else 0 end) adbidcounts,
          |sum (case when adplatformproviderid>=100000 and iseffective=1
          |     and isbilling=1 and iswin=1 then 1 else 0 end) adbidseccesscounts,
          |sum (case when requestmode=2 and iseffective=1 then 1 else 0 end) addispalycounts,
          |sum (case when requestmode=3 and iseffective=1 then 1 else 0 end) adclickcounts,
          |sum (case when requestmode=2 and iseffective=1 and isbilling=1 then 1 else 0 end) mediadispalycounts,
          |sum (case when requestmode=3 and iseffective=1 and isbilling=1 then 1 else 0 end) mediaclickcounts,
          |sum (case when adplatformproviderid>=100000 and iseffective=1
          |     and isbilling=1 and iswin=1 and adorderid>200000 then 1 else 0 end) adconsumecounts,
          |sum (case when adplatformproviderid>=100000 and iseffective=1
          |     and isbilling=1 and iswin=1 and adorderid>200000 then 1 else 0 end) adcostcounts
          |from SYS_AD  GROUP BY province,city) AD order by province,city
        """.stripMargin

        // 满足条件的记录写1，不满足写0
        var result = info
                .withColumn("is_requestmod", when(col("requestmode") === 1 and
                        col("processnode") >= 1, 1).otherwise(0))
                .withColumn("is_processnode",when(col("requestmode") === 1 and
                        col("processnode") >= 2,1).otherwise(0))

        // 统计满足记录个数
        result.groupBy("province","city").agg(sum("is_requestmod").as("requestmodecounts"),
            sum("is_processnode").as("processnodecounts")).orderBy("province","city").
                show(1000)



//        val result = spark.sql(statSql)
//        result.show(1000)
        // 保存到 MYSQL
//        saveToMySQL(spark,result)

        spark.stop()
    }
    case class Info(province:String,city:String,adplatformproviderid:Long,requestmode:Long
                   ,processnode:Long,iseffective:Long,isbilling:Long,isbid:Long,iswin:Long
                   ,adorderid:Long)
}
