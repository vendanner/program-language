package org.danner.bigdata.flink.sink

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.danner.bigdata.flink.utils.ScaLikeJDBCUtil

/**
  * mysql sink
  *
  * domain_traffic 设置主键来更新数据
  *
  */
class MySQLSink extends RichSinkFunction[(Long,String,Long)]{

    val tableName = "domain_traffic1"
    override def open(parameters: Configuration): Unit = {
        super.open(parameters)
    }

    /**
     * 写数据
     *
     * @param value
     * @param context
     */
    override def invoke(value: (Long, String, Long), context: SinkFunction.Context): Unit = {
//        val sql = s"insert into $tableName (time,domain,traffic) " +
//                s"values('${value._1}','${value._2}','${value._3}') " +
//                s"ON DUPLICATE KEY UPDATE time='${value._1}',domain='${value._2}'," +
//                s"traffic='${value._3}'"

        val sql = s"insert into $tableName (time,domain,traffic) " +
                s"values('${value._1}','${value._2}','${value._3}') "

        ScaLikeJDBCUtil.localTx(sql)

    }

    override def close(): Unit = {
        super.close()
    }


}
