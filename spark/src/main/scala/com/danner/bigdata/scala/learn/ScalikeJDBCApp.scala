package com.danner.bigdata.scala.learn

import scalikejdbc.{DB, SQL}
import scalikejdbc.config.DBs

object ScalikeJDBCApp {
    def main(args: Array[String]): Unit = {
        // 初始化 JDBC 配置，加载 application.conf
        DBs.setupAll()

        // 查询示例
        query()
    }
    def query()={
        val offsets = DB.readOnly({
            implicit session => {
                SQL("select * from platform_stat").map(rs => {
                    platform(
                        rs.string("platform"),
                        rs.int("cnt"),
                        rs.string("d")
                    )
                }).list().apply()
            }
        })
        // 输出
        offsets
    }
}
case class platform(platform:String,cnt:Int,time:String)