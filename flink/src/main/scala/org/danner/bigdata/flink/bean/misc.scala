package org.danner.bigdata.flink.bean

object misc {

    case class Access(time:Long,domain:String,traffics:Long)

    case class WC(word:String,count:Long)

    case class PlatformStat(platform:String,cnt:Long,day:String)
}
