package com.danner.bigdata.spark.streaming.homework

import java.util.Calendar

import com.danner.bigdata.utils.RedisUtil
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast

/**
  * 每分钟中从 redis 获取 alert
  */
object BroadcastAlert {

    private var lastUpdatedAt = Calendar.getInstance().getTime
    private val alertSetKey = "prewarning:alert"

    def updateAndGet(sc:SparkContext,bcAlertList:Broadcast[Map[String,String]]):Broadcast[Map[String,String]] = {

        val currentDate = Calendar.getInstance().getTime
        val diff = currentDate.getTime - lastUpdatedAt.getTime
        var bc = bcAlertList

        // 60000 ms = 1 min 刷新一次
        if(bcAlertList == null || diff >= 60000){
            if(bcAlertList != null){
                // 删除原先值
                bcAlertList.unpersist()
            }

            import scala.collection.JavaConversions._
            val jedis = RedisUtil.getJedis()
            val alertWords = jedis.hgetAll(alertSetKey).toMap
            bc = sc.broadcast(alertWords)
            // 释放 redis 连接
            jedis.close()
            lastUpdatedAt = Calendar.getInstance().getTime
        }
        bc
    }
}
