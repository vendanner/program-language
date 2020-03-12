package com.danner.bigdata.spark.sql.mysource

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.{BaseRelation, RelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types.StructType

/**
  * 讲师：若泽(PK哥)
  * 交流群：126181630
  */
class DefaultSource extends RelationProvider with SchemaRelationProvider{
  override def createRelation(
                      sqlContext: SQLContext,
                      parameters: Map[String, String],
                      schema: StructType): BaseRelation = {
    val path = parameters.get("path")

    path match {
      case Some(p) => new TextDatasourceRelation(sqlContext,p,schema)
      case _ => throw  new IllegalArgumentException("path is required...")
    }

  }

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {
    createRelation(sqlContext,parameters,null)
  }
}
