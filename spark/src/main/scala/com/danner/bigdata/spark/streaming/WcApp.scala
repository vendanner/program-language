package com.danner.bigdata.spark.streaming

import com.danner.bigdata.utils.{MySQLUtil, RedisUtil}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import scalikejdbc.{DB, SQL}
import scalikejdbc.config.DBs

/**
  * spark streaming wc
  * 可以将结果存入 MySQL：driver 批量提交方式、scalikejdbc
  * redis，线程池，可实现状态的累计
  */
object WcApp {
    def main(args: Array[String]): Unit = {
        val HOSTNAME = "192.168.22.147"
        val PORT = 9999
        val KEY = "WcApp_key"

        val conf = new SparkConf().setMaster("local[2]").setAppName(WcApp.getClass.getSimpleName)
        val ssc = new StreamingContext(conf,Seconds(10))

        val dStream = ssc.socketTextStream(HOSTNAME,PORT)

        val result = dStream.flatMap(_.split(",")).map((_,1)).reduceByKey(_+_)
//        result.print()

        result.cache()
        println("---------")

        // redis
        result.foreachRDD(rdd =>{
            if(!rdd.isEmpty()){
                // 每个分区既一个 task 共用 redis 连接
                rdd.foreachPartition(partition =>{
                    val jedis = RedisUtil.getJedis()
                    // hash ，wc 统计
                    partition.foreach(pair =>{
                        jedis.hincrBy(KEY,pair._1,pair._2)
                    })
                    jedis.close()
                })
            }
        })

        // MySQL 批量
//        result.foreachRDD(rdd => {
//            if(!rdd.isEmpty()){
//                rdd.foreachPartition(partition => {
//                    val connection = MySQLUtil.getConnection
//                    val sql = "insert into ssc_wc values(?,?)"
//                    connection.setAutoCommit(false)
//                    val statement = connection.prepareStatement(sql)
//                    partition.foreach(pair => {
////                        val sql = s"insert into ssc_wc(word,count) values('${pair._1}',${pair._2})"
//                        statement.setString(1,pair._1)
//                        statement.setInt(2,pair._2)
//                        statement.executeUpdate()
//                    })
//                    // 批量提交
//                    connection.commit()
//
//                    MySQLUtil.closeConnection(connection)
//                })
//            }
//        })

        // scalikejdbc
        // 初始化
//        DBs.setupAll()
//        result.foreachRDD(rdd =>{
//            if(!rdd.isEmpty()){
//                rdd.foreachPartition(partition =>{
//                    partition.foreach(pair =>{
//                        // sclikejdbc 提交
//                        DB.autoCommit{implicit session =>{
//                            val sql = s"insert into ssc_wc(word,count) values(?,?)"
//                            SQL(sql).bind(pair._1,pair._2).update().apply()
//                        }}
//                    })
//                })
//            }
//        })

        ssc.start()
        ssc.awaitTermination()

    }

}
