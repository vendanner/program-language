package com.danner.bigdata.spark.sql.mysource

import org.apache.spark.sql.types._

/**
  * 讲师：若泽(PK哥)
  * 交流群：126181630
  */
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
