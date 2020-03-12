package com.danner.bigdata.spark.core.homework

import com.danner.bigdata.utils.{ContextUtils, HDFSUtil}
import com.danner.bigdata.utils.ImplicitAspect._
import javafx.application.Platform
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat
import org.apache.spark.HashPartitioner

/**
  * Android 类型的只要：ruozedata1 182.86.190.207  2271 江西  景德镇
  * iOS 类型的只要：ruozedata1 182.86.190.207  2271 江西
  * ==> 有能力的扩展成可以自定义根据域名配置来生成
  */
object MultiOutputApp {
    def main(args: Array[String]): Unit = {
        val sc = ContextUtils.getSparkContext(this.getClass.getSimpleName)

        val output = "output/access"
        HDFSUtil.clearFile(output,sc.hadoopConfiguration)

        val access = sc.textFile("input/access.log").map(x => {
            val splits = x.split("\t")
            (splits(1), AccessInfo(splits(0), splits(1), splits(2).toFloat, splits(3), splits(4).toInt,
                splits(5), splits(6).toInt, splits(7), splits(8), splits(9)))
        })
        access.cache()

        val size = access.map(_._1).distinct().count()
        access.partitionBy(new HashPartitioner(size.toInt)).saveAsHadoopFile(output,
            classOf[String],classOf[AccessInfo],classOf[AccessMultipleTextOutputFormat])

        sc.stop()
    }
}
// zhao	iOS	3.0	210.45.152.129	9633	2019-09-19	392	comId	安徽	淮南	教育网
case class AccessInfo(user:String,platform:String,version: Float,ip:String,traffic:Int,date:String,duration:Int,
                      url:String,province:String,city:String){
    override def toString: String = {
        user + "\t" + platform + "\t" + version + "\t" + ip + "\t" + traffic + "\t" + date  + "\t" + duration +
        "\t" + url + "\t" + province + "\t" + city
    }
}

/**
  * 自定义输出
  */
class AccessMultipleTextOutputFormat extends MultipleTextOutputFormat[Any,Any]{

    // platform 固定字段
    val platformFieldsMap = Map("iOS" -> Array("url","ip","province","city"),
                                    "Android" -> Array("url","ip","traffic","date") )

    override def generateFileNameForKeyValue(key: Any, value: Any, name: String): String = {
        s"$key/$name"
    }

    override def generateActualKey(key: Any, value: Any): AnyRef = {
        NullWritable.get()
    }

    override def generateActualValue(key: Any, value: Any): AnyRef = {
        val accessInfo = value.asInstanceOf[AccessInfo]
        val platformFileds = platformFieldsMap.getOrElse(key.asInstanceOf[String],Array())
        if(platformFileds.nonEmpty){
            // 反射获取字段值
            val ru = scala.reflect.runtime.universe
            val m = ru.runtimeMirror(getClass.getClassLoader)
            val im = m.reflect(accessInfo)

            val sb = new StringBuffer()
            for(str <- platformFileds){
                val nameTermSymb = ru.typeOf[AccessInfo].decl(ru.TermName(str)).asTerm
                sb.append(im.reflectField(nameTermSymb).get + "\t")
            }
            sb.substring(0,sb.length()-1)
        }else{
            NullWritable.get()
        }

//        key match {
//            case "iOS" => {
//                accessInfo.getClass.
//                val url = accessInfo.url
//                val ip = accessInfo.ip
//                val province = accessInfo.province
//                val city = accessInfo.city
//                url + "\t" + ip + "\t" + province + "\t" + city
//            }
//            case "Android" => {
//                val url = accessInfo.url
//                val ip = accessInfo.ip
//                val traffic = accessInfo.traffic
//                val date = accessInfo.date
//                url + "\t" + ip + "\t" + traffic + "\t" + date
//            }
//        }
    }
}