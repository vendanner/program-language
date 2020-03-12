package com.danner.bigdata.utils

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.{Jedis, JedisCluster, JedisPool}

object RedisUtil {
    val HOSTNAME = "hadoop002"
    val PORT = 6379

    // 可设置连接池大小
    private val config = new GenericObjectPoolConfig
    private val jedisPool = new JedisPool(config,HOSTNAME,PORT)

    def getJedis():Jedis={
        jedisPool.getResource
    }
}
