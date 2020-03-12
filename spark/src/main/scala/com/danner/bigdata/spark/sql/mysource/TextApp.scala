package com.danner.bigdata.spark.sql.mysource

import org.apache.spark.sql.SparkSession

/**
  */
object TextApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("TextApp").master("local[2]").getOrCreate()

    val df = spark.read.format("com.danner.bigdata.spark.sql.mysource").option("path","input/1.txt").load()

    df.createOrReplaceTempView("test")
    spark.sql("select * from test where comm < 300").show()
    df.persist()


    Thread.sleep(100000)
    spark.stop()
  }

}
