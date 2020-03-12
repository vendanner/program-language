package com.danner.bigdata.spark.streaming.homework.batch

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.util.SizeEstimator

/**
  * spark sql 加载数据到 hive
  * 1、将flume 抽取到hdfs 上的日志文件 移动到 hive 表下
  * 2、刷数据到 hive
  * 3、必须保证整条链路是可重跑
  * 4、coalesce 解决小文件问题
  */
object LoadData extends Logging{

    /**
      * @param context
      * @param hivePath
      * @return
      */
    def deleteHiveDir(context:Configuration,hivePath:String):Boolean={
        val fileSystem = FileSystem.get(context)

        if(fileSystem.exists(new Path(hivePath))){
            val flag = fileSystem.delete(new Path(hivePath),true)
            if(!flag){
                logError("删除 " + hivePath + " 失败")
                return false
            }
        }
        true
    }
    def main(args: Array[String]): Unit = {

        val inputPath = "hdfs://hadoop001:8020/project/spark/prewarning/log/"
        val hivePath = "hdfs://hadoop001:8020/project/spark/prewarning/hive/"
        val day = "20191121"
        val size = 128

        // 配置访问用户
        System.setProperty("HADOOP_USER_NAME","hadoop")
        // Hive 操作要设置 enableHiveSupport //
        val spark = SparkSession.builder().appName("ForamtApp").master("local[3]")
                .config("dfs.client.use.datanode.hostname","true")  // hostname 访问，一般是外网使用
                .enableHiveSupport()
                .getOrCreate()

        val configuration = spark.sparkContext.hadoopConfiguration
        configuration.set("dfs.client.use.datanode.hostname","true")
        configuration.set("fs.defaultFS","hdfs://hadoop001:8020")

       // 删除 hive 路径下之前的数据
        val hiveDir = hivePath+"day="+day
        if(deleteHiveDir(configuration,hiveDir)){
            // 验证数据
            val rdd = spark.sparkContext.textFile(inputPath+day)
            val isValidCHDLog = (content:String) => {
                // 这里安装需求应该是过滤出 warn、error，
                // 但这两种等级的日志没有这么多暂且不过滤
                if (content.contains("INFO") ||
                        content.contains("WARN")  ||
                        content.contains("ERROR") ||
                        content.contains("DEBUG")||
                        content.contains("FATAL")) {
                    true
                }else{
                    false
                }
            }
            val logInfo = rdd.filter(isValidCHDLog)
            // 数据输出到 hive 下
            import spark.implicits._
            val dataSet = spark.createDataset(logInfo)

            // 调整分区减少小文件 estimate 一般是比实际大小 10 倍
            var partiions = SizeEstimator.estimate(dataSet)/1024/1024/10/size
            if(partiions < 1){
                partiions = 1
            }

            dataSet.coalesce(partiions.toInt).write.mode(SaveMode.Overwrite)
                    .format("text")
                    .option("compression","bzip2")
                    .save(hiveDir)

            // hive 数据更新
            spark.sql("msck repair table rz_db.prewarn_log")
            println(day + " hive 数据刷新成功")

        }
        spark.stop()
    }

}


