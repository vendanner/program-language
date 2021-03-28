package com.danner.bigdata.spark.sql.mysource

import org.apache.spark.sql.types._

object Utils {

  def castTo(value:String, dataType:DataType) ={
    dataType match {
      case _:DoubleType => value.toDouble
      case _:LongType => value.toLong
      case _:StringType => value
      case _:IntegerType => value.toInt
    }
  }

}
