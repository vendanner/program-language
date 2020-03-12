package bigdata.emp.v1

import bigdata.emp.utils.EmpParser
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.util.LongAccumulator


/**
  * 日志清洗
  */
object EmpETLV1App  extends Logging{
  def main(args: Array[String]): Unit = {
      System.setProperty("HADOOP_USER_NAME","hadoop")
    val spark = SparkSession.builder().master("local[4]").appName("EmpETLV1App")
            .config("dfs.client.use.datanode.hostname","true") .getOrCreate()


      val appName = spark.sparkContext.getConf.get("spark.app.name")


    val time = "2019060811"

      val day = time.substring(0,8)
      val hour = time.substring(8,10)
      val input = s"/ruozedata/offline/emp/raw/$time"
      val output = s"/ruozedata/offline/emp/col/"
//    val output = s"/ruozedata/offline/emp/col/day=$day/hour=$hour"

      spark.sparkContext.getConf.set("dfs.client.use.datanode.hostname","true")

1
      // 运行时间
      val start = System.currentTimeMillis()
      // 数据质量
      val total: LongAccumulator = spark.sparkContext.longAccumulator("total")
      val errors = spark.sparkContext.longAccumulator("errors")

    try{
        val partition = getCoalesce(FileSystem.get(spark.sparkContext.hadoopConfiguration),input+"/*").toInt
        var logDF = spark.read.format("text").load(input).coalesce(partition)

        logDF = spark.createDataFrame(logDF.rdd.map(x => {
            EmpParser.parseLog(x.getString(0),total,errors)
        }).filter(_.length == 6),EmpParser.struct)

        logDF.show(false)
        logDF.write.format("parquet").partitionBy("day","hour").option("compression","none").mode(SaveMode.Overwrite).save(output)
//        logDF.write.format("parquet").option("compression","none").mode(SaveMode.Overwrite).save(output)
        val end = System.currentTimeMillis()
        logError(s"${appName} run use time ${(end - start)/1000}, totals = ${total.value},errors = ${errors.value}")
    }catch {
        case e => logError(s"${appName} run exception => ${e.printStackTrace()}")
    }finally {
        if(null != spark){
            spark.stop()
        }
    }
  }

    def getCoalesce(fileSytem:FileSystem,path:String,size:Long=100):Long ={
        var sum = 0l
        fileSytem.globStatus(new Path(path)).map(x => {
            logError(s"file path => ${x.getPath.toString}")
            sum += x.getLen
        })
        sum/1024/1024/size.toInt + 1
    }
}
