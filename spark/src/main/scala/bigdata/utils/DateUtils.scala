package bigdata.utils


import java.util.{Date, Locale}
import org.apache.commons.lang3.time.FastDateFormat
import org.apache.spark.internal.Logging


/**
  * 使用 FastDateFormat 线程安全
  */
object DateUtils extends Logging {

    val SOURCE_TIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

    val TARGET_TIME_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss", Locale.ENGLISH)

  def getTime(time: String):Long = {
    try {
      SOURCE_TIME_FORMAT.parse(time).getTime

    } catch {
      case e: Exception =>
        logError(s"$time parse error: ${e.getMessage}")
        0l
    }
  }

  def parseToMinute(time: String) = {
    TARGET_TIME_FORMAT.format(new Date(getTime(time)))
  }

  def getDay(minute: String) = {
    minute.substring(0, 8)
  }

  def getHour(minute: String) = {
    minute.substring(8, 10)
  }

  def main(args: Array[String]): Unit = {
    println(parseToMinute("2019-11-30 10:14:09"))
    println(getDay(parseToMinute("2019-11-30 10:14:09")))
    println(getHour(parseToMinute("2019-11-30 10:14:09")))
  }
}
