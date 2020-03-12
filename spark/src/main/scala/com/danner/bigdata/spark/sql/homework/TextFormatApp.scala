package com.danner.bigdata.spark.sql.homework

import org.apache.spark.sql._
import org.apache.spark.sql.expressions.Window

/**
  * text 支持多列输出
  *
  * text 保存时数据只能一列(TextFileFormat.verifySchema 函数校验)
  * 所以在保存之前先想办法将数据由多列压缩成单列，concat_ws : 用指定的字符连接字符
  */
object TextFormatApp {
    def main(args: Array[String]): Unit = {
        val spark = SparkSession.builder().appName("ForamtApp").master("local[2]").getOrCreate()

        import spark.implicits._
        val value = spark.read.textFile("input/sql/people.txt")
        val ds = value.map(x => {
            val splits = x.split(",")
            val name = splits(0)
            val age = splits(1).trim
            (name , age)
        })

        multiColumsSaveToText(ds.toDF())

        spark.close()
    }
    def multiColumsSaveToText(df:DataFrame,delimiter:String = ","):Unit={
        val colums = df.columns.mkString(",")
        // 字段以 "," 合并
//        df.printSchema()
        // concat_ws 指定 delimiter 分割符，合并列
        Window.partitionBy().orderBy()
        val all_column = functions.concat_ws(delimiter,df.col("_1"),df.col("_2"))
        val result = df.select(all_column).as("colums_name")
//        val result = df.selectExpr(s"concat_ws('$delimiter',$colums) as colums_name")
        result.write.mode(SaveMode.Overwrite).format("text").save("out")
    }
}

