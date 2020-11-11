package com.danner.bigdata.spark.sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SparkSession, functions}

object DataFrame {

  /**
    * RDD 转 DataFrame/DataSet
    * @param spark
    * @param input
    */
  def createFromCaseClassRDD(spark:SparkSession,input: RDD[PandaPlace]) = {
    import spark.implicits._

    // Create DataFrame explicitly using session and schema inference
    val df1 = spark.createDataFrame(input)
    // Create DataFrame using session implicits and schema inference
    val df2 = input.toDF()

    // Create a Row RDD from our RDD of case classes
    val rowRDD = input.map(pm => {
      Row(pm.name,pm.pandas.map(pi => {
        Row(pi.id, pi.zip, pi.happy, pi.attributes)
      }))
    })

    val pandasType = ArrayType(StructType(List(
      StructField("id", LongType, true),
      StructField("zip", StringType, true),
      StructField("happy", BooleanType, true),
      StructField("attributes", ArrayType(FloatType), true))))
    // Create DataFrame explicitly with specified schema
    val schema = StructType(List(StructField("name", StringType, true),
      StructField("pandas", pandasType)))

    val df3 = spark.createDataFrame(rowRDD, schema)

    val ds1 = input.toDS()
  }

  /**
    * dataFrame 转 RDD
    * 1. 先转成 RDD[ROW]
    * 2. ROW --> case class
    * @param input
    * @return
    */
  def toRDD(input: DataFrame): RDD[RawPanda] = {
    val rdd: RDD[Row] = input.rdd
    rdd.map(row => RawPanda(row.getAs[Long](0), row.getAs[String](1),
      row.getAs[String](2), row.getAs[Boolean](3), row.getAs[Array[Double]](4)))
  }


  /**
    * 获取子节点
    * @param spark
    * @param df
    */
  def subNode(spark:SparkSession,df:DataFrame)={

    // 获取数组[0],expr 函数可以计算表达式
    df.select(df("pandas").alias("array_col"))
      .select(functions.expr("array_col[0]"))
      .show(false)

    df.select(df("pandas").alias("array_col"))
      .select(functions.expr("array_col[0]"))
      .printSchema()

    // explode
    val pandaInfo = df.explode(df("pandas")){
      case Row(pandas: Seq[Row]) =>
        pandas.map{
          case (Row(
          id: Long,
          zip: String,
          pt: String,
          happy: Boolean,
          attrs: Seq[Double])) =>
            RawPanda(id, zip, pt, happy, attrs.toArray)
        }}
    pandaInfo.printSchema()
    pandaInfo.select(pandaInfo("attributes")).show(false)

  }

  def json(spark: SparkSession):Unit = {

    val df = spark.read.format("json").load("input/sql/rawpanda.json")
    subNode(spark,df)

  }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("ForamtApp")
      .master("local[2]")
      .enableHiveSupport()
      .getOrCreate()

    json(spark)



    spark.stop()
  }
}

case class PandaPlace(name: String,pandas: Array[RawPanda])
case class RawPanda(id: Long, zip: String, pt: String,
                    happy: Boolean, attributes: Array[Double])
