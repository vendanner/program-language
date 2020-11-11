package com.danner.bigdata.spark.sql

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._

/**
  * udaf 定义和使用
  */
object UDFApp {

  def setupUDAFs(spark:SparkSession): Unit ={
    class Avg extends UserDefinedAggregateFunction {
      // 以下四个属性都是继承
      // Input type
      def inputSchema: org.apache.spark.sql.types.StructType =
        StructType(StructField("value", DoubleType) :: Nil)
      def bufferSchema: StructType = StructType(
        StructField("count", LongType) ::
          StructField("sum", DoubleType) :: Nil
      )
      // Return type
      def dataType: DataType = DoubleType
      def deterministic: Boolean = true

      def initialize(buffer: MutableAggregationBuffer): Unit = {
        buffer(0) = 0L
        buffer(1) = 0.0
      }
      def update(buffer: MutableAggregationBuffer,input: Row): Unit = {
        buffer(0) = buffer.getAs[Long](0) + 1
        buffer(1) = buffer.getAs[Double](1) + input.getAs[Double](0)
      }
      def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
        buffer1(0) = buffer1.getAs[Long](0) + buffer2.getAs[Long](0)
        buffer1(1) = buffer1.getAs[Double](1) + buffer2.getAs[Double](1)
      }
      def evaluate(buffer: Row): Any = {
        buffer.getDouble(1) / buffer.getLong(0)
      }
    }

    // Optionally register
    val avg = new Avg
    spark.udf.register("ourAvg", avg)
  }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("ForamtApp")
      .master("local[2]")
      .enableHiveSupport()
      .getOrCreate()

      setupUDAFs(spark)
  }

}
