package bigdata.emp.utils

import bigdata.utils.DateUtils
import org.apache.kafka.common.metrics.stats.Total
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.apache.spark.util.LongAccumulator

/**
  * 日志解析工具类
  * 严格字段丢失，则丢弃该数据
  * 非严格字段丢失，应保留该字段
  */
object EmpParser {

  // schema
  val struct = StructType(Array(
    StructField("id", IntegerType),
    StructField("name", StringType),
    StructField("salary", DoubleType),
    StructField("time", LongType),
    StructField("day", StringType),
    StructField("hour", StringType)
  ))

  // parse log
  def parseLog(log: String,total: LongAccumulator,errors:LongAccumulator): Row = {
    try {
        total.add(1)

      val splits = log.split("\t")
      val id = splits(0).toInt
      val name = splits(1)

      val time = DateUtils.getTime(splits(2))
      val minute = DateUtils.parseToMinute(splits(2))
      val day = DateUtils.getDay(minute)
      val hour = DateUtils.getHour(minute)
      // 不是严格字段，不能丢弃数据
      var salary = 0D
      try{
          salary = splits(3).toDouble
      }catch {
          case e => salary = 0D
      }
      Row(
        id, name,salary, time, day, hour
      )
    } catch {
      case e: Exception => e.printStackTrace()
          errors.add(1)
        Row(0)
    }
  }
}