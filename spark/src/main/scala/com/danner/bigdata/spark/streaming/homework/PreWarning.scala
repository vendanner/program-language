//package com.danner.bigdata.spark.streaming.homework
//
//import com.danner.bigdata.utils.InfluxDBUtil
//import org.apache.kafka.common.serialization.StringDeserializer
//import org.apache.spark.broadcast.Broadcast
//import org.apache.spark.sql.SparkSession
//import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
//import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils}
//import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
//import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}
//import org.influxdb.{InfluxDB, InfluxDBFactory}
//
///**
//  * 预警系统
//  * 1、kafka 获取数据
//  * 2、分析数据
//  * 3、分析结果存 influxDB
//  * 4、grafana 从 influxDB 获取数据展示
//  */
//object PreWarning {
//    def main(args: Array[String]): Unit = {
//
//        val interval = 5
//        val windowLength = 5
//        val topic = "PREWARNING"
//        val groupId = "ruozedata"
//        val dbName = "ruozedata"
//        var bcAlertList:Broadcast[Array[String]] = null
//
//        val spark = SparkSession.builder()
//                .master("local[3]")
//                .appName("PreWarningTest")
//                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//                // spark.sql.shuffle.partitions 默认200，本例不需要这么多task
//                // 查看task 中 shuffle 时有数据的个数，然后再设置该值
//                .config("spark.sql.shuffle.partitions","5")
//                .getOrCreate()
//        val sc = spark.sparkContext
//        val ssc = new StreamingContext(sc,Seconds(interval))
//
//        val kafkaParams = Map[String, Object](
//            "bootstrap.servers" -> "hadoop002:9092,hadoop004:9092,hadoop003:9092",
//            "key.deserializer" -> classOf[StringDeserializer],
//            "value.deserializer" -> classOf[StringDeserializer],
//            "group.id" -> groupId,
//            "auto.offset.reset" -> "latest",
//            "enable.auto.commit" -> (false: java.lang.Boolean)
//        )
//
//        val dStream = KafkaUtils.createDirectStream[String, String](ssc,
//            PreferConsistent,
//            Subscribe[String, String](Array(topic), kafkaParams))
//
//        val isValidCHDLog = (content:String) => {
//            if (content.contains("INFO") ||
//                    content.contains("WARN")  ||
//                    content.contains("ERROR") ||
//                    content.contains("DEBUG")||
//                    content.contains("FATAL")) {
//                true
//            }else{
//                false
//            }
//        }
//        // 处理log 并将结果存入 influxdb
//        dStream.foreachRDD(rdd => {
//            if(!rdd.isEmpty()){
//
//                // 1. 获取 offset
//                val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
//
//                // 2.业务处理
//                import  spark.implicits._
//                // 2.1 取出无效的 log
//                val log = rdd.map(_.value()).filter(isValidCHDLog)
//
//                // 2.2 log 转化为 df
//                // RDD[String] 先转换为 DataSet
//                // DataSet 再转化为 DataFrame
//                val ds = spark.createDataset(log)
//                val df = spark.read.json(ds)
//                df.show(false)
//
//                //  2.3 注册表 spark SQL 来处理
//                df.createOrReplaceTempView("prewarninglogs")
//
//                // 2.4 数据分析
//                var sql = "SELECT hostname,servicename,logtype,COUNT(logtype) count  " +
//                        "FROM prewarninglogs GROUP BY hostname,servicename,logtype"
//                // 更新 alert 词
//                bcAlertList = BroadcastAlert.updateAndGet(sc,bcAlertList)
//                val alertWords = bcAlertList.value
//                if(alertWords.length > 0){
//                    var alertSql = ""
//                    println("alert word:")
//                    for(word <- alertWords){
//                        println(word + "")
//                        alertSql = alertSql + " loginfo like '%" + word + "%' or"
//                    }
//                    alertSql = alertSql.substring(0,alertSql.length - 2)
//                    sql = "SELECT hostname,servicename,logtype,COUNT(logtype) count FROM prewarninglogs GROUP BY " +
//                            "hostname,servicename,logtype union all " +
//                            "SELECT t.hostname,t.servicename,t.logtype,COUNT(t.logtype) count FROM " +
//                            "(SELECT hostname,servicename,'alert' as logtype FROM prewarninglogs where "+alertSql+") t " +
//                            " GROUP BY t.hostname,t.servicename,t.logtype";
//                }
//                println(sql)
//                val logTypeCounts = spark.sql(sql).as[logTypeCount]
//                val value = logTypeCounts.repartition(4)
//
//                // 2.5 结果存入 influxdb
//                value.foreachPartition(partition => {
//                    var value = ""
//                    partition.foreach(row => {
//                        val hostServiceLogtype = row.hostName + "_" + row.serviceName + "_" + row.logType
//                        value = value + "prewarning,host_service_logtype="+hostServiceLogtype +
//                        " count=" + String.valueOf(row.count) + "\n"
//                    })
//                    if(!value.isEmpty){
//                        // influxdb 初始化
//                        val influxDB = InfluxDBFactory.connect("http://" + InfluxDBUtil.getInfluxIP + ":" +
//                                InfluxDBUtil.getInfluxPORT(true), "admin", "admin")
//                        val rp = InfluxDBUtil.defaultRetentionPolicy(influxDB.version())
//                        value = value.substring(0,value.length())
//                        println("insert influxdb:" + value)
//                        influxDB.write(dbName,rp,InfluxDB.ConsistencyLevel.ONE,value)
//                    }
//                })
//
//                // 3.业务逻辑处理结束后，提交 offset
//                dStream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
//            }else{
//                println("no cdh role logs in this time interval")
//            }
//
//
//        })
//
//
//        ssc.start()
//        ssc.awaitTermination()
//    }
//    case class logTypeCount(hostName:String,serviceName:String,logType:String,count:Long)
//}
//
