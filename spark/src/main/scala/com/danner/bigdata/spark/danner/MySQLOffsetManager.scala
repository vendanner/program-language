package com.danner.bigdata.spark.danner

import com.danner.bigdata.utils.{MiscUtil, ScaLikeJDBCUtil}
import org.apache.kafka.common.TopicPartition
import scalikejdbc.{DB, SQL, WrappedResultSet}

/**
  * mysql 中 offset 更新用 upset 语法，注意要设置主键 = MD5(topic + ":" + groupId + ":" + partition)
  * create table wc_offset(
  * primaryKey varchar(32) PRIMARY KEY,
  * topic varchar(64),
  * groupid varchar(64),
  * partiton int,
  * offset bigint
  * )
  */
object MySQLOffsetManager extends OffsetManager {

    var tableName = "wc_offset"

    def setTableName(tableName:String)={
        this.tableName = tableName
    }
    /**
      * 保存 offset
      * @param topic
      * @param groupId
      * @param partition
      * @param offset
      */
    override def storeOffsets(topic :String,groupId: String,partition:Int,offset:Long): Unit = {
        // 准备 primary key
        val primaryKey = MiscUtil.hashMD5(topic + ":" + groupId + ":" + partition)
        val sql = s"insert into $tableName (primaryKey,topic,groupid,partiton,offset) " +
                s"values('$primaryKey','$topic','$groupId','$partition','$offset') " +
                s"ON DUPLICATE KEY UPDATE topic='$topic',groupid='$groupId'," +
                s"partiton='$partition',offset='$offset'"

        ScaLikeJDBCUtil.localTx(sql)
    }

    /**
      * 获取 offset
      * @param topic
      * @param groupId
      * @return
      */
    override def obtainOffsets(topic: String, groupId: String): Map[TopicPartition, Long] = {
        val sql = s"select * from $tableName where topic='$topic' and groupid='$groupId'"
        val function = (rs: WrappedResultSet) => {
            val topic = rs.string(2)
            val partition = rs.int(4)
            val offset = rs.long(5)
            new TopicPartition(topic, partition) -> offset
        }

        val result = ScaLikeJDBCUtil.query(sql,function)
        result.toMap
    }

    def main(args: Array[String]): Unit = {
//        val topic = "test"
//        val groupId = "test_group"
//        val tuples: List[(TopicPartition, Long)] = DB.readOnly({
//            implicit session => {
//                val sql = s"select * from wc_offset where topic='${topic}' and groupid='${groupId}'"
//                SQL(sql).map(x => {
//                    val topic = x.string(1)
//                    val partition = x.int(3)
//                    val offset = x.long(4)
//                    new TopicPartition(topic, partition) -> offset
//                }).list().apply()
//            }
//        })
//        println(tuples.toMap)
//        storeOffsets("test","test_group",0,39)
//        storeOffsets("test","test_group",1,39)
        print(obtainOffsets("test","test_group"))

    }
}
