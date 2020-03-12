package com.danner.bigdata.spark.danner

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.rdd.RDD

trait OffsetManager {
    def storeOffsets(topic :String,groupId: String,partition:Int,offset:Long):Unit
    def obtainOffsets(topic :String,groupId:String):Map[TopicPartition,Long]
}
