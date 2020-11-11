package org.danner.bigdata.flink.bean

import java.text.SimpleDateFormat

object misc {

    case class Access(time:Long,domain:String,traffics:Long)

    case class WC(word:String,count:Long)

    case class PlatformStat(platform:String,cnt:Long,day:String)

    def getLongTime(time: String): Long = {
        var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sdf.parse(time).getTime
    }
}
