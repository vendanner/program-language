package bigdata.emp.v1

import bigdata.emp.utils.EmpParser
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.util.LongAccumulator


/**
  * 日志清洗
  * 1、参数命令行传递
  * 2、数据质量：total、errors(非严格字段错误，该数据不能丢弃)
  * 3、分区数 - 根据文件大小动态设置，可以调节输出文件数
  */
object EmpETLV1AppShell  extends Logging{
  def main(args: Array[String]): Unit = {
      System.setProperty("HADOOP_USER_NAME","hadoop")

      val spark = SparkSession.builder()
              .config("dfs.client.use.datanode.hostname","true") .getOrCreate()

      val appName = spark.sparkContext.getConf.get("spark.app.name")

      val time = spark.sparkContext.getConf.get("spark.time",null)
      if(null == time){
          logError("spark.time is empty")
          System.exit(0)
      }
      val input = spark.sparkContext.getConf.get("spark.input",s"/ruozedata/offline/emp/raw/$time")
      val output = spark.sparkContext.getConf.get("spark.output",s"/ruozedata/offline/emp/col/")

      spark.sparkContext.getConf.set("dfs.client.use.datanode.hostname","true")
    val day = time.substring(0,8)
    val hour = time.substring(8,10)


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
        // partitionBy("day","hour").mode(SaveMode.Overwrite) 文件覆盖问题?
        // 分区数：时间正确数据多，时间错误数据少，但出来的分区数是相同的，导致时间错误数据的小文件多
        logDF.write.format("parquet").partitionBy("day","hour").mode(SaveMode.Overwrite).save(output)

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
