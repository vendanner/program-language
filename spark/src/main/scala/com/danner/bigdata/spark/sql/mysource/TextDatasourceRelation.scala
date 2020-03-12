package com.danner.bigdata.spark.sql.mysource

import org.apache.hadoop.fs.FileStatus
import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types._

import scala.collection.mutable.ListBuffer

class TextDatasourceRelation(override val sqlContext: SQLContext,
                             path:String,
                             userSchema:StructType)
  extends BaseRelation  with  TableScan  with PrunedScan with PrunedFilteredScan with Serializable
    with Logging{

  override def schema: StructType = {
    if(userSchema != null){
      userSchema
    } else {
      StructType(
        StructField("id",IntegerType,false) ::
          StructField("name",StringType,false) ::
          StructField("gender",StringType,false) ::
          StructField("salary",DoubleType,false) ::
          StructField("comm",DoubleType,false) :: Nil
      )
    }
  }

  // select * from xxx
  override def buildScan(): RDD[Row] = {
    logError("this is ruozedata custom buildScan...")

    var rdd = sqlContext.sparkContext.wholeTextFiles(path).map(_._2)
    val schemaField = schema.fields

    // rdd + schemaField
    val rows = rdd.map(fileContent => {
      val lines = fileContent.split("\n")
      val data = lines.map(_.split(",").map(x=>x.trim)).toSeq

      val result = data.map(x => x.zipWithIndex.map{
        case  (value, index) => {

          val columnName = schemaField(index).name

          Utils.castTo(if(columnName.equalsIgnoreCase("gender")) {
            if(value == "0") {
              "男"
            } else if(value == "1"){
              "女"
            } else {
              "未知"
            }
          } else {
            value
          }, schemaField(index).dataType)
        }
        })
      result.map(x => Row.fromSeq(x))
    })

    rows.flatMap(x=>x)
  }

  // select column_name from table
  override def buildScan(requiredColumns: Array[String]): RDD[Row] = {
    logError("PrunedScan: buildScan called...")

    var rdd = sqlContext.sparkContext.wholeTextFiles(path).map(_._2)
    val schemaFields = schema.fields

    val schemaFieldsName = schemaFields.map(x => x.name)
    val requiredColumnsIndex = new ListBuffer[Int]
    for(column <- requiredColumns){
      val index = schemaFieldsName.indexOf(column)
      requiredColumnsIndex.append(index)
    }
    val rows = rdd.map(fileContext =>{
      val lines = fileContext.split("\n")
      val data = lines.map(x => x.split(",").map(x => x.trim).toSeq)
      val genderMap = Map("0" ->"男","1" -> "女")
      val typeedValue = data.map(x => {
        // 过滤列
        requiredColumnsIndex.map(index => {
          val colName = schemaFields(index).name
          val value = x(index)
          val valueTo = Utils.castTo(if(colName.equalsIgnoreCase("gender")){
            genderMap.getOrElse(value,"未知")
          }else{
            value
          } ,schemaFields(index).dataType)
          valueTo
        })
      })
      // 将 Seq的数据转换成Row
      typeedValue.map(x => Row.fromSeq(x))
    })
    // 所有的行都在一起，转换成一行一行
    rows.flatMap(x => x)
  }

  // select column_name from table where
  override def buildScan(requiredColumns: Array[String], filters: Array[Filter]): RDD[Row] = {
    logError("PrunedFilteredScan: buildScan called...")

    val filterFuncs = filters.map(filter => createFilterFunction(filter))

    val rdd = sqlContext.sparkContext.wholeTextFiles(path).map(x => x._2)
    // 获取所有字段
    val schemaFields = schema.fields

    /**
      * 如何根据schema的field的数据类型以及字段顺序整合到rdd
      */
    val schemaFieldsName = schemaFields.map(x => x.name)
    val requiredColumnsIndex = new ListBuffer[Int]
    for(column <- requiredColumns){
      // 提前做好数据筛选
      val index = schemaFieldsName.indexOf(column)
      requiredColumnsIndex.append(index)
    }
    val rows = rdd.map(fileContext =>{
      val lines = fileContext.split("\n")
      val data = lines.map(x => x.split(",").map(x => x.trim).toSeq)
      val genderMap = Map("0" ->"男","1" -> "女")

      val typeedValue = data.filter(x => {
        // filter
        if(filterFuncs.forall(_.apply(x))){
          true
        }else{
          false
        }
      }).map(x => {
        // 过滤列
        requiredColumnsIndex.map(index => {
          val colName = schemaFields(index).name
          val value = x(index)
          val valueTo = Utils.castTo(if(colName.equalsIgnoreCase("gender")){
            genderMap.getOrElse(value,"未知")
          }else{
            value
          } ,schemaFields(index).dataType)
          valueTo
        })
      })
      // 将 Seq的数据转换成Row
      typeedValue.map(x => Row.fromSeq(x))

    })
    // 所有的行都在一起，转换成一行一行,
    rows.flatMap(x => x)
  }


  private def createFilterFunction(filter: Filter): Seq[String] => Boolean = {
    val genderMap = Map("0" ->"男","1" -> "女")
    filter match {
      case LessThan(attribute:String,value:Double) =>
        val index = schema.fieldIndex(attribute)
        _.apply(index).toDouble < value
      case _ => (_ => true)
    }
  }
}
