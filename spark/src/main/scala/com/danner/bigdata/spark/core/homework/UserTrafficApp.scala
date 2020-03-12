package com.danner.bigdata.spark.core.homework

import com.danner.bigdata.utils.ContextUtils
import com.danner.bigdata.utils.ImplicitAspect._

import scala.util.Sorting

/**
  * 输入数据：
  *     user.txt(id,city,name)  空格分隔
  * 1000001 bj douyin
  * 1000002 sh yy
  * 1000003 bj douyu
  * 1000004 sz qqmusic
  * 1000005 gz huya
  *     traffics.txt(id,year,month,traffic)
  * 1000001 2019 9 90
  * 1000002 2019 12 20
  * 1000003 2019 9 4
  * 1000003 2019 7 5
  * 1000003 2019 8 6
  * 结果：
  * 1000001 bj douyin,2019 9 90
  * 1000002 sh yy,2019 12 20
  * 1000004 sz qqmusic,null null null
  * 1000003 bj douyu,2019 7 5|2019 9 4|2017 8 6  年份降序 月份升序
  * 1000005 gz huya,null null null
  */
object UserTrafficApp {
    def main(args: Array[String]): Unit = {

        val sc = ContextUtils.getSparkContext(this.getClass.getSimpleName)

        val userRdd = sc.textFile("input/user.txt").map(x => {
            val words = x.split(" ")
            val userId = words(0)
            val addr = words(1)
            val app = words(2)
            (userId, (addr, app))
        })
        val traffic = sc.textFile("input/traffics.txt").map(x => {
            val words = x.split(" ")
            val userId = words(0)
            val year = words(1).toInt
            val month = words(2).toInt
            val day = words(3).toInt
            val traffic = Traffic(year,month,day)
            (userId,Array(traffic))
        })
        // traffic 先排序，然后再和 user join
        val trafficRdd = traffic.reduceByKey(_ ++ _).mapValues(x => {
            Sorting.quickSort(x)
            x
        }).mapValues(x => {
            var sb = new StringBuffer()
            for (i <- x) {
                sb.append(i.year + " " + i.month + " " + i.d + "|")
            }
            sb.substring(0, sb.length() - 1)
        })
        userRdd.leftOuterJoin(trafficRdd).map(x => {
            val userId = x._1
            val addr = x._2._1._1
            val app = x._2._1._2
            val str = x._2._2.getOrElse("null null null")
            (userId + " " + addr + " " + app,str)
        }).printInfo()

        sc.stop()
    }
}

/**
  * 排序
  * @param year
  * @param month
  * @param d
  */
case class Traffic(year:Int,month:Int,d:Int) extends Ordered[Traffic]{
    override def compare(that: Traffic): Int = {
       this.year == that.year match {
           case false => that.year - this.year
           case _ => this.month - that.month
       }
    }
}
