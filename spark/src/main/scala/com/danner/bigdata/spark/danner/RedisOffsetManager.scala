package com.danner.bigdata.spark.danner

import com.danner.bigdata.utils.RedisUtil
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.TaskContext
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, OffsetRange}

object RedisOffsetManager extends OffsetManager {

    /**
      * 保存 offset
      * @param topic
      * @param groupId
      * @param partition
      * @param offset
      */
    override def storeOffsets(topic :String,groupId: String,partition:Int,offset:Long): Unit = {
        val jedis = RedisUtil.getJedis()
        jedis.hset(topic + ":" + groupId,String.valueOf(partition),String.valueOf(offset))
        jedis.close()

    }

    /**
      * 获取 offset
      * @param topic
      * @param groupId
      * @return
      */
    override def obtainOffsets(topic: String, groupId: String): Map[TopicPartition, Long] = {
        import scala.collection.JavaConversions._
        val jedis = RedisUtil.getJedis()
        val offsets = jedis.hgetAll(topic + ":" + groupId)
        val fromOffsets = offsets.toMap.map(x =>{
            new TopicPartition(topic, x._1.toInt) -> x._2.toLong
        })
        jedis.close()
        fromOffsets
    }
}
