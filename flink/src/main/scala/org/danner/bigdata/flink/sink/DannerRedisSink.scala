package org.danner.bigdata.flink.sink

import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

/**
  * redis sink 实现 RedisMapper 即可
  * RedisMapper[(String, Long)] 保存的 (String, Long) 类型
  */
class DannerRedisSink extends RedisMapper[(String, Long)]{
    val redisKey = "domain_traffic"
    override def getCommandDescription: RedisCommandDescription = {
        new RedisCommandDescription(RedisCommand.HSET, redisKey)
    }

    override def getKeyFromData(data: (String, Long)): String = {
        data._1
    }
    override def getValueFromData(data: (String, Long)): String = {
        data._2.toString
    }
}