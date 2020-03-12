package org.danner.bigdata.flink.source

import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.danner.bigdata.flink.bean.misc.PlatformStat
import org.danner.bigdata.flink.utils.ScaLikeJDBCUtil
import scalikejdbc.WrappedResultSet

/**
  * mysql 数据源
  */
class MySQLSource extends SourceFunction[Map[String,String]]{

    /**
      * 获取数据
      * @param ctx
      */
//    override def run(ctx: SourceFunction.SourceContext[PlatformStat]): Unit = {
//        val sql = "select * from platform_stat"
//        val function = (rs: WrappedResultSet) => {
//            val platform = rs.string("platform")
//            val cnt = rs.long("cnt")
//            val day = rs.string("d")
//            ctx.collect(PlatformStat(platform,cnt,day))
//        }
//        ScaLikeJDBCUtil.query(sql,function)
//    }
    override def run(ctx: SourceFunction.SourceContext[Map[String,String]]): Unit = {
        val sql = "select * from word_int"
        val function = (rs: WrappedResultSet) => {
            val word = rs.string("word")
            val traffic = rs.long("traffic")
            (word,traffic.toString)
        }
        val map: Map[String, String] = ScaLikeJDBCUtil.query(sql,function).toMap
        if(map.nonEmpty){
            ctx.collect(map)
        }
    }

    override def cancel(): Unit = {

    }
}
