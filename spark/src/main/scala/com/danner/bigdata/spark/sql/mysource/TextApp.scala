package com.danner.bigdata.spark.sql.mysource

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.types._

/**
  */
object TextApp {

  def main(args: Array[String]): Unit = {

    val schema = StructType(
      StructField("id",IntegerType,false) ::
        StructField("name",StringType,false) :: Nil
    )

    val spark = SparkSession.builder().appName("TextApp").master("local[2]").getOrCreate()

    val df = spark.read.format("com.danner.bigdata.spark.sql.mysource").option("path","input/1.txt").load()

    df.createOrReplaceTempView("test")
    spark.sql("select * from test where comm < 300").show()
    df.persist()

   val row =  new GenericRowWithSchema(Array(Seq(1),Seq("li")), schema)
    print(row.getAs[Int]("id"))
    Thread.sleep(100000)
    spark.stop()
  }

}
