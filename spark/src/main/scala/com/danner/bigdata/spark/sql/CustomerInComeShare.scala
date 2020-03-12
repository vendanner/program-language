//package com.danner.bigdata.spark.sql
//
//import com.ruozedata.common.utils.{ConnConfig, DateUtil, UDFUtils}
//import org.apache.log4j.Logger
//import org.apache.phoenix.spark._
//import org.apache.spark.sql.{Row, SparkSession}
//import org.apache.hadoop.conf.Configuration
//import org.apache.spark.sql.expressions._
//import org.apache.spark.sql.functions._
//import org.apache.spark.sql.types.{DoubleType, StructField, StructType}
//
//
///**
//  *
//  * Created by Jepson on 2018/1/11.
//  */
//object CustomerInComeShare {
//
//    var logger = Logger.getLogger("CustomerInComeShare")
//
//    val spark = SparkSession
//            .builder()
//            .appName("总部管理费用-仓干配")
//            .master("local[4]")
//            .getOrCreate()
//
//    val configuration = new Configuration()
//    configuration.set(ConnConfig.ZKQUORUM, ConnConfig.ZKURL)
//
//    val udfUtil = new UDFUtils
//    spark.sqlContext.udf.register("cost_share_func", udfUtil.cost_share_func(_: java.math.BigDecimal, _: java.math.BigDecimal, _: java.math.BigDecimal, _: java.math.BigDecimal))
//    spark.sqlContext.udf.register("re_cost_share_func", udfUtil.re_cost_share_func(_: java.math.BigDecimal, _: java.math.BigDecimal, _: java.math.BigDecimal, _: Boolean))
//
//    val firstDay = "2017-10-01"
//    val lastDay = DateUtil.getLastMonthEnd()
//    val previousMonth = DateUtil.getPreviousMonth()
//    val currentMonth = DateUtil.getCurrentMonth()
//
//    //val specifiedMonth = previousMonth
//
//    def main(args: Array[String]): Unit = {
//        val beginTime = System.currentTimeMillis()
//        customerInComeShare()
//        System.out.println(">>>>>共用时：" + (System.currentTimeMillis() - beginTime))
//    }
//
//    def customerInComeShare() = {
//
//
//        // 1.获得指定月的总部其他管理费     "WAREHOUSE_CODE"不带
//        //    val fees_pay_importDF = spark.sqlContext.phoenixTableAsDataFrame(
//        //      "JYDW.bms_fees_pay_import",
//        //      Array("AMOUNT","CREATE_MONTH"),
//        //      predicate = Some(
//        //        """
//        //          | subject_code = 'cp_other' and create_month = 'cremonth'
//        //        """.stripMargin.replace("cremonth", specifiedMonth)),
//        //      conf = configuration
//        //    )
//        val fees_pay_importDF = spark.sqlContext.phoenixTableAsDataFrame(
//            "JYDW.bms_fees_pay_import",
//            Array("AMOUNT","CREATE_MONTH"),
//            predicate = Some(
//                """
//                  | subject_code = 'cp_other'
//                """.stripMargin),
//            conf = configuration
//        )
//        fees_pay_importDF.createOrReplaceTempView("cp_other_fees_sum")
//
//        //2.获得商家 仓库 科目代码 金额 折扣金融  时间
//
//        //2.1仓租 人工 耗材
//        //    val bms_fees_receive_storageDF = spark.sqlContext.phoenixTableAsDataFrame(
//        //      "JYDW.bms_fees_receive_storage",
//        //      Array("CUSTOMER_ID","WAREHOUSE_CODE","SUBJECT_CODE","COST","DERATE_AMOUNT","CREATE_TIME","ORDER_NO"),
//        //      predicate = Some(
//        //        """
//        //          | to_char(CREATE_TIME, 'yyyy-MM') = 'month'
//        //        """.stripMargin.replace("month", specifiedMonth)),
//        //      conf = configuration
//        //    )
//        val bms_fees_receive_storageDF = spark.sqlContext.phoenixTableAsDataFrame(
//            "JYDW.bms_fees_receive_storage",
//            Array("CUSTOMER_ID","WAREHOUSE_CODE","SUBJECT_CODE","COST","DERATE_AMOUNT","CREATE_TIME","ORDER_NO"),
//            predicate = Some(
//                """
//                  | CUSTOMER_ID is not null and WAREHOUSE_CODE is not null and SUBJECT_CODE is not null and CREATE_TIME is not null
//                """.stripMargin),
//            conf = configuration
//        )
//        bms_fees_receive_storageDF.createOrReplaceTempView("bms_fees_receive_storage")
//
//        //2.2干线 虽然表有SUBJECT_CODE，但是后面处理为一个全体数据都为干线  ; "DERATE_AMOUNT" 库中无字段
//        //    val bms_fees_receive_transportDF = spark.sqlContext.phoenixTableAsDataFrame(
//        //      "JYDW.bms_fees_receive_transport",
//        //      Array("CUSTOMERID","WAREHOUSE_CODE","SUBJECT_CODE","TOTLEPRICE","CRETIME"),
//        //      predicate = Some(
//        //        """
//        //          | to_char(CRETIME, 'yyyy-MM')  = 'month'
//        //        """.stripMargin.replace("month", specifiedMonth)),
//        //      conf = configuration
//        //    )
//
//
//        val bms_fees_receive_transportDF = spark.sqlContext.phoenixTableAsDataFrame(
//            "JYDW.bms_fees_receive_transport",
//            Array("CUSTOMERID","WAREHOUSE_CODE","SUBJECT_CODE","TOTLEPRICE","CRETIME","WEIGHT","ORDERNO"),
//            predicate = Some(
//                """
//                  | CUSTOMERID is not null and WAREHOUSE_CODE is not null and SUBJECT_CODE is not null and CRETIME is not null
//                """.stripMargin),
//            conf = configuration
//        )
//        bms_fees_receive_transportDF.createOrReplaceTempView("bms_fees_receive_transport")
//
//        //2.3宅配  不需要SUBJECT_CODE，后面处理为一个全体数据都为宅配
//        //    val bms_fees_receive_dispatchDF = spark.sqlContext.phoenixTableAsDataFrame(
//        //      "JYDW.bms_fees_receive_dispatch",
//        //      Array("CUSTOMERID","WAREHOUSE_CODE","AMOUNT","DERATE_AMOUNT","CREATE_TIME"),
//        //      predicate = Some(
//        //        """
//        //          | to_char(CREATE_TIME, 'yyyy-MM') = 'month'
//        //        """.stripMargin.replace("month", specifiedMonth)),
//        //      conf = configuration
//        //    )
//
//        val bms_fees_receive_dispatchDF = spark.sqlContext.phoenixTableAsDataFrame(
//            "JYDW.bms_fees_receive_dispatch",
//            Array("CUSTOMERID","WAREHOUSE_CODE","AMOUNT","DERATE_AMOUNT","CREATE_TIME","CHARGED_WEIGHT","OUTSTOCK_NO","TOTAL_WEIGHT"),
//            predicate = Some(
//                """
//                  | CUSTOMERID is not null and WAREHOUSE_CODE is not null and CREATE_TIME is not null
//                """.stripMargin),
//            conf = configuration
//        )
//        bms_fees_receive_dispatchDF.createOrReplaceTempView("bms_fees_receive_dispatch")
//
//        //    //获得客户
//        //    val customer_idDF = spark.sql(
//        //      """
//        //        |SELECT
//        //        |DISTINCT
//        //        |CUSTOMER_ID
//        //        |FROM
//        //        |(
//        //        |SELECT CUSTOMER_ID  FROM bms_fees_receive_storage
//        //        |UNION ALL
//        //        |SELECT CUSTOMERID   FROM bms_fees_receive_transport
//        //        |UNION ALL
//        //        |SELECT CUSTOMERID   FROM bms_fees_receive_dispatch
//        //        |)
//        //      """.stripMargin)
//        //    customer_idDF.show(100)
//
//        /**
//          *仓租 人工  耗材找对应的subject_code
//          * select * from bms_bms_subject_info where DIMEN2_CODE = 'INPUT_RENT'; -- 仓租
//        select * from bms_bms_subject_info where DIMEN2_CODE = 'INPUT_ARTIFIC'; -- 人工
//        select * from bms_bms_subject_info where DIMEN2_CODE = 'INPUT_MATERIAL'; -- 耗材
//          *
//          */
//        val bms_bms_subject_infoDF = spark.sqlContext.phoenixTableAsDataFrame(
//            "JYDW.bms_bms_subject_info",
//            Array("SUBJECT_CODE","DIMEN2_CODE"),
//            predicate = Some(
//                """
//                  | DIMEN2_CODE = 'INPUT_RENT' OR DIMEN2_CODE = 'INPUT_ARTIFIC' OR DIMEN2_CODE = 'INPUT_MATERIAL'
//                """.stripMargin),
//            conf = configuration
//        )
//        bms_bms_subject_infoDF.createOrReplaceTempView("bms_bms_subject_info")
//
//
//
//        //    //3.获得仓库
//        //    val warehouse_codeDF = spark.sql(
//        //      """
//        //        |SELECT distinct WAREHOUSE_CODE from bms_fees_receive_storage
//        //      """.stripMargin)
//
//
//
//        //****************************4.第一阶段分摊到商家：按商家收入金额****************************
//        //仓租 人工 耗材 干线 宅配 收入  group by聚合计算
//        val customerInComeDF= spark.sql(
//            """
//              |select
//              |
//              |t.customer_id,t.warehouse_code,t.subject_code,t.cretime_month,
//              |
//              |cast(
//              | (   cast(sum(t.cost) as decimal(16,6)) - cast(sum(case when isnull(t.derate_amount) then 0 else t.derate_amount end) as decimal(16,6))   )
//              | as decimal(16,6)
//              | )  as sum_cost
//              |
//              |from
//              |(
//              |select
//              |customer_id,warehouse_code,'wh_cp_other_rent' as subject_code,date_format(create_time,'yyyyMM') as cretime_month,cost,derate_amount
//              |from bms_fees_receive_storage
//              |where (other_subject_code in (select subject_code from bms_bms_subject_info where DIMEN2_CODE = 'INPUT_RENT' ))
//              |
//              |
//              |union all
//              |
//              |select
//              |customer_id,warehouse_code,'wh_cp_other_staff' as subject_code,date_format(create_time,'yyyyMM') as cretime_month,cost,derate_amount
//              |from bms_fees_receive_storage
//              |where (other_subject_code in (select subject_code from bms_bms_subject_info where DIMEN2_CODE = 'INPUT_ARTIFIC' ))
//              |
//              |
//              |union all
//              |
//              |select
//              |customer_id,warehouse_code,'wh_cp_other_material' as subject_code,date_format(create_time,'yyyyMM') as cretime_month,cost,derate_amount
//              |from bms_fees_receive_storage
//              |where (other_subject_code in (select subject_code from bms_bms_subject_info where DIMEN2_CODE = 'INPUT_MATERIAL' ))
//              |
//              |
//              |union all
//              |
//              |select
//              |customerid,warehouse_code,'ts_cp_other' as subject_code,date_format(cretime,'yyyyMM') as cretime_month,totleprice,0 as derate_amount
//              |from bms_fees_receive_transport
//              |
//              |union all
//              |
//              |select
//              |customerid,warehouse_code,'de_cp_other' as subject_code,date_format(create_time,'yyyyMM') as cretime_month,amount,derate_amount
//              |from bms_fees_receive_dispatch
//              |
//              |) t
//              |
//              |group by t.customer_id,t.warehouse_code,t.subject_code,t.cretime_month
//            """.stripMargin)
//        //customerInComeDF.show(100)
//        customerInComeDF.createOrReplaceTempView("customerInCome")
//
//        //left join 总费用  left join分组总费用
//        //
//        val customerInComeSumFeesDF= spark.sql(
//            """
//              |select
//              |customer_id,warehouse_code,subject_code,cretime_month,
//              |sum_other_fee,sum_cost,monthgroupby_sum_cost,
//              |cast(cost_share_func(sum_other_fee, sum_cost, 1, monthgroupby_sum_cost) as decimal(16,6)) as res_fees
//              |
//              |from
//              |
//              |(
//              |
//              |select
//              |a.customer_id,a.warehouse_code,a.subject_code,a.cretime_month,
//              |case when isnull(b.sum_other_fee) then 0 else b.sum_other_fee end  as sum_other_fee ,
//              |a.sum_cost,
//              |c.monthgroupby_sum_cost
//              |
//              |
//              |from customerInCome a
//              |
//              |left join
//              |(select cast(create_month as string) as create_month,cast( sum(amount) as decimal(16,6) ) as sum_other_fee  from cp_other_fees_sum group by cast(create_month as string)) b
//              |on a.cretime_month=b.create_month
//              |
//              |left join
//              |(select cretime_month,sum(sum_cost) as monthgroupby_sum_cost              from customerInCome group by cretime_month) c
//              |on a.cretime_month=c.cretime_month
//              |
//              |)
//            """.stripMargin)  //monthgroupby_sum_cost > 0 表示分母为0的过滤掉
//        customerInComeSumFeesDF.createOrReplaceTempView("customerInComeSumFees")
//        //TODO: 测试SQL: select * from customerInComeSumFees where customer_id='1100000619'
//
//        //使用LAG函数平移等 将差值加到最大值上
//        val customerInComeRuleDF = spark.sql(
//            """
//              |select customer_id,warehouse_code,subject_code,cretime_month,res_fees_update as res_fees
//              |
//              |from
//              |(
//              |    select
//              |        customer_id,warehouse_code,subject_code,cretime_month,
//              |        sum_other_fee,sum_cost,monthgroupby_sum_cost,res_fees,
//              |        sumaccumulationvalue,islast,
//              |        cast(re_cost_share_func(res_fees, sum_other_fee, sumaccumulationvalue, islast) as decimal(16,6)) as res_fees_update
//              |
//              |
//              |    from
//              |        ( select
//              |            customer_id,warehouse_code,subject_code,cretime_month,
//              |            sum_other_fee,sum_cost,monthgroupby_sum_cost,res_fees,
//              |
//              |            cast(sum(res_fees)   over (partition by cretime_month order by res_fees asc) as decimal(16,6)) as sumaccumulationvalue,
//              |            isnull(lag(res_fees) over (partition by cretime_month order by res_fees desc)) as islast
//              |        from
//              |        customerInComeSumFees
//              |        )
//              |)
//            """.stripMargin)
//        //customerInComeRuleDF.show()
//        // 行转列
//        customerInComeRuleDF
//                .groupBy("customer_id","warehouse_code","cretime_month")
//                .pivot("subject_code", Seq("wh_cp_other_rent", "wh_cp_other_staff","wh_cp_other_material","ts_cp_other","de_cp_other"))
//                .agg(max("res_fees"))
//                .createOrReplaceTempView("customerInComeRule")
//        //TODO: 测试SQL: select * from customerInComeRule where customer_id='1100000619'
//
//        // null to 0 ,总部管理费第一阶段 仓库(仓租 人工 耗材) 结束
//        val cpOtherFees_First_WH = spark.sql(
//            """
//              |select
//              |
//              |customer_id,warehouse_code,cretime_month,
//              |case when isnull(wh_cp_other_rent) then 0 else wh_cp_other_rent end  as wh_cp_other_rent ,
//              |case when isnull(wh_cp_other_staff) then 0 else wh_cp_other_staff end  as wh_cp_other_staff ,
//              |case when isnull(wh_cp_other_material) then 0 else wh_cp_other_material end  as wh_cp_other_material
//              |
//              |from customerInComeRule
//            """.stripMargin)
//        cpOtherFees_First_WH.createOrReplaceTempView("cpOtherFees_First_WH")
//
//        //null to 0 ,总部管理费第一阶段 干线 结束
//        val cpOtherFees_First_TS = spark.sql(
//            """
//              |select
//              |
//              |customer_id,warehouse_code,cretime_month,
//              |case when isnull(ts_cp_other) then 0 else ts_cp_other end  as ts_cp_other
//              |
//              |from customerInComeRule
//            """.stripMargin)
//        cpOtherFees_First_TS.createOrReplaceTempView("cpOtherFees_First_TS")
//
//        //null to 0 ,总部管理费第一阶段 配送 结束
//        val cpOtherFees_First_DE = spark.sql(
//            """
//              |select
//              |
//              |customer_id,warehouse_code,cretime_month,
//              |case when isnull(de_cp_other) then 0 else de_cp_other end  as de_cp_other
//              |
//              |from customerInComeRule
//            """.stripMargin)
//        cpOtherFees_First_DE.createOrReplaceTempView("cpOtherFees_First_DE")
//
//
//        //****************************5.第二阶段分摊到订单：按订单调整重量(仓租 人工 耗材) 和 按订单计费重量(干线 配送)****************************
//        //按订单调整重量 先按TOTAL_WEIGHT计算
//        //    val bms_biz_outstock_masterDF = spark.sqlContext.phoenixTableAsDataFrame(
//        //      "JYDW.bms_biz_outstock_master",
//        //      Array("WAREHOUSE_CODE","CUSTOMERID","CREATE_TIME","OUTSTOCK_NO","TOTAL_WEIGHT","RESIZE_WEIGHT"),
//        //      predicate = Some(
//        //        """
//        //          | to_char(CREATE_TIME, 'yyyy-MM') = 'month'
//        //        """.stripMargin.replace("month", specifiedMonth)),
//        //      conf = configuration
//        //    )
//        //5.1按订单调整总量 和仓储 人工 耗材搭配
//        val bms_biz_outstock_masterDF = spark.sqlContext.phoenixTableAsDataFrame(
//            "JYDW.bms_biz_outstock_master",
//            Array("WAREHOUSE_CODE","CUSTOMERID","CREATE_TIME","OUTSTOCK_NO","TOTAL_WEIGHT","RESIZE_WEIGHT"),
//            conf = configuration
//        )
//        bms_biz_outstock_masterDF.createOrReplaceTempView("bms_biz_outstock_master")
//
//        //TODO:  使用total_weight作为resize_weight
//        spark.sql(
//            """
//              |select
//              |
//              |customerid,warehouse_code,date_format(create_time,'yyyyMM') as cretime_month ,outstock_no as orderno,total_weight as resize_weight,
//              |
//              |cast(sum(total_weight) over (partition by customerid,warehouse_code,date_format(create_time,'yyyyMM')  ) as decimal(16,6))  as sum_resize_weight
//              |
//              |from
//              |bms_biz_outstock_master
//            """.stripMargin).createOrReplaceTempView("orderResizeWeight")
//
//        //5.2按订单计费总量 和干线 搭配
//        spark.sql(
//            """
//              |select
//              |
//              |customerid,warehouse_code,date_format(cretime,'yyyyMM') as cretime_month ,orderno,weight,
//              |
//              |cast(sum(weight) over (partition by customerid,warehouse_code,date_format(cretime,'yyyyMM')  ) as decimal(16,6))  as sum_weight
//              |
//              |from
//              |bms_fees_receive_transport
//            """.stripMargin).createOrReplaceTempView("orderWeight")
//
//        //5.3按订单计费总量 和配送 搭配
//        //TODO: 使用total_weight作为charged_weight
//        spark.sql(
//            """
//              |select
//              |
//              |customerid,warehouse_code,date_format(CREATE_TIME,'yyyyMM') as cretime_month ,outstock_no as orderno,total_weight as charged_weight,
//              |
//              |cast(sum(total_weight) over (partition by customerid,warehouse_code,date_format(create_time,'yyyyMM')  ) as decimal(16,6))  as sum_charged_weight
//              |
//              |from
//              |bms_fees_receive_dispatch
//            """.stripMargin).createOrReplaceTempView("orderChargedWeight")
//
//
//
//        //5.1.1 计算仓储 人工 耗材的
//        val orderRentStaffMaterialDF = spark.sql(
//            """
//              |select
//              |
//              |a.customer_id,a.warehouse_code,a.cretime_month,
//              |a.wh_cp_other_rent,a.wh_cp_other_staff,a.wh_cp_other_material,
//              |b.orderno,b.resize_weight,b.sum_resize_weight,
//              |
//              |cast(cost_share_func(a.wh_cp_other_rent, b.resize_weight, 1, b.sum_resize_weight) as decimal(16,6)) as order_rent,
//              |cast(cost_share_func(a.wh_cp_other_staff, b.resize_weight, 1, b.sum_resize_weight) as decimal(16,6)) as order_staff,
//              |cast(cost_share_func(a.wh_cp_other_material, b.resize_weight, 1, b.sum_resize_weight) as decimal(16,6)) as order_material
//              |
//              |from cpOtherFees_First_WH a
//              |
//              |left join orderResizeWeight b
//              |on a.customer_id = b.customerid and a.warehouse_code = b.warehouse_code and  a.cretime_month = b.cretime_month
//            """.stripMargin)  //测试数据 and a.warehouse_code='B03' and a.cretime_month='201712'
//        orderRentStaffMaterialDF.createOrReplaceTempView("orderRentStaffMaterial")
//
//        //5.1.2 将余数加到最大值上
//        val orderRentStaffMaterialDF_Rule = spark.sql(
//            """
//              |select
//              |
//              |customer_id,warehouse_code,cretime_month,wh_cp_other_rent,wh_cp_other_staff,wh_cp_other_material,
//              |orderno,resize_weight,sum_resize_weight,
//              |order_rent_update as      order_rent,
//              |order_staff_update as     order_staff,
//              |order_material_update as  order_material
//              |
//              |
//              |from
//              |(   select
//              |        customer_id,warehouse_code,cretime_month,wh_cp_other_rent,wh_cp_other_staff,wh_cp_other_material,
//              |        orderno,resize_weight,sum_resize_weight,
//              |
//              |        order_rent,sumaccumulationvalue_rent,islast_rent,
//              |        cast(re_cost_share_func(order_rent, wh_cp_other_rent, sumaccumulationvalue_rent, islast_rent) as decimal(16,6)) as order_rent_update,
//              |
//              |        order_staff,sumaccumulationvalue_staff,islast_staff,
//              |        cast(re_cost_share_func(order_staff, wh_cp_other_staff, sumaccumulationvalue_staff, islast_staff) as decimal(16,6)) as order_staff_update,
//              |
//              |        order_material,sumaccumulationvalue_material,islast_material,
//              |        cast(re_cost_share_func(order_material, wh_cp_other_material, sumaccumulationvalue_material, islast_material) as decimal(16,6)) as order_material_update
//              |
//              |    from
//              |        (
//              |         select
//              |
//              |            customer_id,warehouse_code,cretime_month,wh_cp_other_rent,wh_cp_other_staff,wh_cp_other_material,
//              |            orderno,resize_weight,sum_resize_weight,
//              |
//              |            order_rent,
//              |            cast(sum(order_rent)   over (partition by customer_id,warehouse_code,cretime_month order by order_rent asc) as decimal(16,6)) as sumaccumulationvalue_rent,
//              |            isnull(lag(order_rent) over (partition by customer_id,warehouse_code,cretime_month order by order_rent desc)) as islast_rent,
//              |
//              |            order_staff,
//              |            cast(sum(order_staff)   over (partition by customer_id,warehouse_code,cretime_month order by order_staff asc) as decimal(16,6)) as sumaccumulationvalue_staff,
//              |            isnull(lag(order_staff) over (partition by customer_id,warehouse_code,cretime_month order by order_staff desc)) as islast_staff,
//              |
//              |            order_material,
//              |            cast(sum(order_material)   over (partition by customer_id,warehouse_code,cretime_month order by order_material asc) as decimal(16,6)) as sumaccumulationvalue_material,
//              |            isnull(lag(order_material) over (partition by customer_id,warehouse_code,cretime_month order by order_material desc)) as islast_material
//              |
//              |        from
//              |        orderRentStaffMaterial
//              |        )
//              |)
//            """.stripMargin)
//        orderRentStaffMaterialDF_Rule.createOrReplaceTempView("orderRentStaffMaterial_Rule")
//        //TODO：测试SQL: select * from orderRentStaffMaterial_Rule  WHERE warehouse_code='B03'
//
//
//        //5.2.1 计算干线
//        val orderTSDF = spark.sql(
//            """
//              |select
//              |
//              |a.customer_id,a.warehouse_code,a.cretime_month,
//              |a.ts_cp_other,
//              |b.orderno,b.weight,b.sum_weight,
//              |
//              |cast(cost_share_func(a.ts_cp_other, b.weight, 1, b.sum_weight) as decimal(16,6)) as order_ts
//              |
//              |from cpOtherFees_First_TS a
//              |
//              |left join orderWeight b
//              |on a.customer_id = b.customerid and a.warehouse_code = b.warehouse_code and  a.cretime_month = b.cretime_month
//            """.stripMargin)  //测试数据 and a.warehouse_code='B03' and a.cretime_month='201712'
//        orderTSDF.createOrReplaceTempView("orderTS")
//
//        //5.2.2 将余数加到最大值上
//        val orderTSDF_Rule = spark.sql(
//            """
//              |select
//              |
//              |customer_id,warehouse_code,cretime_month,ts_cp_other,
//              |orderno,weight,sum_weight,
//              |order_ts_update as order_ts
//              |
//              |
//              |from
//              |(   select
//              |        customer_id,warehouse_code,cretime_month,ts_cp_other,
//              |        orderno,weight,sum_weight,
//              |
//              |        order_ts,sumaccumulationvalue_ts,islast_ts,
//              |        cast(re_cost_share_func(order_ts, ts_cp_other, sumaccumulationvalue_ts, islast_ts) as decimal(16,6)) as order_ts_update
//              |    from
//              |        (
//              |         select
//              |
//              |            customer_id,warehouse_code,cretime_month,ts_cp_other,
//              |            orderno,weight,sum_weight,
//              |
//              |            order_ts,
//              |            cast(sum(order_ts)   over (partition by customer_id,warehouse_code,cretime_month order by order_ts asc) as decimal(16,6)) as sumaccumulationvalue_ts,
//              |            isnull(lag(order_ts) over (partition by customer_id,warehouse_code,cretime_month order by order_ts desc)) as islast_ts
//              |
//              |        from
//              |        orderTS
//              |        )
//              |)
//            """.stripMargin)
//        orderTSDF_Rule.createOrReplaceTempView("orderTS_Rule")
//        //TODO：测试SQL: select * from orderTS_Rule where   order_de > 0 ,然后选取  1100000619	B02	201708
//
//
//        //5.3.1 计算配送
//        val orderDEDF = spark.sql(
//            """
//              |select
//              |
//              |a.customer_id,a.warehouse_code,a.cretime_month,
//              |a.de_cp_other,
//              |b.orderno,b.charged_weight,b.sum_charged_weight,
//              |
//              |cast(cost_share_func(a.de_cp_other, b.charged_weight, 1, b.sum_charged_weight) as decimal(16,6)) as order_de
//              |
//              |from cpOtherFees_First_DE a
//              |
//              |left join orderChargedWeight b
//              |on a.customer_id = b.customerid and a.warehouse_code = b.warehouse_code and  a.cretime_month = b.cretime_month
//            """.stripMargin)  //测试数据 and a.warehouse_code='B03' and a.cretime_month='201712'
//        orderDEDF.createOrReplaceTempView("orderDE")
//
//        //5.3.2 将余数加到最大值上
//        val orderDEDF_Rule = spark.sql(
//            """
//              |select
//              |
//              |customer_id,warehouse_code,cretime_month,de_cp_other,
//              |orderno,charged_weight,sum_charged_weight,
//              |order_de_update as order_de
//              |
//              |
//              |from
//              |(   select
//              |        customer_id,warehouse_code,cretime_month,de_cp_other,
//              |        orderno,charged_weight,sum_charged_weight,
//              |
//              |        order_de,sumaccumulationvalue_de,islast_de,
//              |        cast(re_cost_share_func(order_de, de_cp_other, sumaccumulationvalue_de, islast_de) as decimal(16,6)) as order_de_update
//              |    from
//              |        (
//              |         select
//              |
//              |            customer_id,warehouse_code,cretime_month,de_cp_other,
//              |            orderno,charged_weight,sum_charged_weight,
//              |
//              |            order_de,
//              |            cast(sum(order_de)   over (partition by customer_id,warehouse_code,cretime_month order by order_de asc) as decimal(16,6)) as sumaccumulationvalue_de,
//              |            isnull(lag(order_de) over (partition by customer_id,warehouse_code,cretime_month order by order_de desc)) as islast_de
//              |
//              |        from
//              |        orderDE
//              |        )
//              |)
//            """.stripMargin)
//        orderDEDF_Rule.createOrReplaceTempView("orderDE_Rule")
//        //TODO：测试SQL: select * from orderDE_Rule where   order_de > 0 ,然后选取  1100000615	B03	 201712
//
//
//
//        //****************************6.第三阶段分摊到SKU：按SKU重量(仅干线 配送)****************************
//        /**
//          *
//          * select
//        a.ORDERNO,
//        a.PRODUCTID,
//        a.PRODUCTNAME,
//        b.GROSSWEIGHT,
//        a.QTY
//        from OMS_ORDERINFOITEM a left join pub_product  b  on a.PRODUCTID = b.PRODUCTID
//          */
//        val oms_orderinfoitemDF = spark.sqlContext.phoenixTableAsDataFrame(
//            "JYDW.oms_orderinfoitem",
//            Array("ORDERNO","PRODUCTID","QTY"),
//            conf = configuration
//        )
//        oms_orderinfoitemDF.createOrReplaceTempView("oms_orderinfoitem")
//
//        val pub_productDF = spark.sqlContext.phoenixTableAsDataFrame(
//            "JYDW.PUB_PRODUCT",
//            Array("PRODUCTID","GROSSWEIGHT"),
//            conf = configuration
//        )
//        pub_productDF.createOrReplaceTempView("pub_product")
//
//        //订单 sku 重量
//        val skuDF = spark.sql(
//            """
//              |  SELECT
//              |    ORDERNO,PRODUCTID,GROSSWEIGHT_SUM,
//              |    cast(sum(GROSSWEIGHT_SUM) over (partition by ORDERNO  ) as decimal(16,6)) as GROSSWEIGHT_SUM_GROUPBYORDER
//              |  from
//              |  (
//              |  select
//              |  ORDERNO,PRODUCTID,
//              |  cast( sum(GROSSWEIGHT_SUM) as decimal(16,6) ) as GROSSWEIGHT_SUM
//              |    FROM
//              |    (
//              |        select
//              |        a.ORDERNO,
//              |        a.PRODUCTID,
//              |        case when isnull(b.GROSSWEIGHT) then 0 else b.GROSSWEIGHT end as GROSSWEIGHT,
//              |        a.QTY,
//              |        cast((case when isnull(b.GROSSWEIGHT) then 0 else b.GROSSWEIGHT end)*a.QTY as decimal(16,6) ) as GROSSWEIGHT_SUM
//              |        from OMS_ORDERINFOITEM a
//              |        left join pub_product  b  on a.PRODUCTID = b.PRODUCTID
//              |    )
//              |  group by ORDERNO,PRODUCTID
//              |  )
//            """.stripMargin)
//        skuDF.createOrReplaceTempView("skuDF")
//
//        //6.1.1 计算干线的SKU
//        val skuTSDF=spark.sql(
//            """
//              |select
//              |a.*,
//              |b.PRODUCTID,
//              |case when isnull(b.GROSSWEIGHT_SUM) then 0 else b.GROSSWEIGHT_SUM end GROSSWEIGHT_SUM,
//              |case when isnull(b.GROSSWEIGHT_SUM_GROUPBYORDER) then 0 else b.GROSSWEIGHT_SUM_GROUPBYORDER end as GROSSWEIGHT_SUM_GROUPBYORDER ,
//              |cast(cost_share_func(a.order_ts, (case when isnull(b.GROSSWEIGHT_SUM) then 0 else b.GROSSWEIGHT_SUM end), 1, (case when isnull(b.GROSSWEIGHT_SUM_GROUPBYORDER) then 0 else b.GROSSWEIGHT_SUM_GROUPBYORDER end)) as decimal(16,6)) as sku_ts
//              |
//              |from orderTS_Rule a
//              |left join
//              |skuDF b  on a.ORDERNO = b.ORDERNO
//            """.stripMargin)
//        skuTSDF.createOrReplaceTempView("skuTSDF")
//
//        //6.1.2 将余数加到最大值上
//        val skuTSDF_Rule = spark.sql(
//            """
//              |select
//              |
//              |            customer_id,warehouse_code,cretime_month,ts_cp_other,
//              |            orderno,weight,sum_weight,order_ts,
//              |            productid,grossweight_sum,grossweight_sum_groupbyorder,sku_ts_update as sku_ts
//              |
//              |
//              |from
//              |(
//              | select
//              |            customer_id,warehouse_code,cretime_month,ts_cp_other,
//              |            orderno,weight,sum_weight,order_ts,
//              |            productid,grossweight_sum,grossweight_sum_groupbyorder,
//              |
//              |        sku_ts,sumaccumulationvalue_ts,islast_ts,
//              |        cast(re_cost_share_func(sku_ts, order_ts, sumaccumulationvalue_ts, islast_ts) as decimal(16,6)) as sku_ts_update
//              |    from
//              |        (
//              |         select
//              |
//              |            customer_id,warehouse_code,cretime_month,ts_cp_other,
//              |            orderno,weight,sum_weight,order_ts,
//              |            productid,grossweight_sum,grossweight_sum_groupbyorder,
//              |
//              |            sku_ts,
//              |            cast(sum(sku_ts)   over (partition by orderno order by sku_ts asc) as decimal(16,6)) as sumaccumulationvalue_ts,
//              |            isnull(lag(sku_ts) over (partition by orderno order by sku_ts desc)) as islast_ts
//              |
//              |        from
//              |        skuTSDF
//              |        )
//              |)
//            """.stripMargin)
//        skuTSDF_Rule.createOrReplaceTempView("skuTSDF_Rule")
//
//
//
//        //6.2.1 计算配送的SKU
//        val skuDEDF=spark.sql(
//            """
//              |select
//              |a.*,
//              |b.PRODUCTID,
//              |case when isnull(b.GROSSWEIGHT_SUM) then 0 else b.GROSSWEIGHT_SUM end as GROSSWEIGHT_SUM,
//              |case when isnull(b.GROSSWEIGHT_SUM_GROUPBYORDER) then 0 else b.GROSSWEIGHT_SUM_GROUPBYORDER end as GROSSWEIGHT_SUM_GROUPBYORDER ,
//              |cast(cost_share_func(a.order_de, (case when isnull(b.GROSSWEIGHT_SUM) then 0 else b.GROSSWEIGHT_SUM end), 1, (case when isnull(b.GROSSWEIGHT_SUM_GROUPBYORDER) then 0 else b.GROSSWEIGHT_SUM_GROUPBYORDER end)) as decimal(16,6)) as sku_de
//              |
//              |from orderDE_Rule a
//              |left join
//              |skuDF b  on a.ORDERNO = b.ORDERNO
//            """.stripMargin)
//        skuDEDF.createOrReplaceTempView("skuDEDF")
//
//        //6.2.2 将余数加到最大值上
//        val skuDEDF_Rule = spark.sql(
//            """
//              |select
//              |
//              |            customer_id,warehouse_code,cretime_month,de_cp_other,
//              |            orderno,charged_weight,sum_charged_weight,order_de,
//              |            productid,grossweight_sum,grossweight_sum_groupbyorder,sku_de_update as sku_de
//              |
//              |
//              |from
//              |(
//              | select
//              |            customer_id,warehouse_code,cretime_month,de_cp_other,
//              |            orderno,charged_weight,sum_charged_weight,order_de,
//              |            productid,grossweight_sum,grossweight_sum_groupbyorder,
//              |
//              |        sku_de,sumaccumulationvalue_de,islast_de,
//              |        cast(re_cost_share_func(sku_de, order_de, sumaccumulationvalue_de, islast_de) as decimal(16,6)) as sku_de_update
//              |    from
//              |        (
//              |         select
//              |
//              |            customer_id,warehouse_code,cretime_month,de_cp_other,
//              |            orderno,charged_weight,sum_charged_weight,order_de,
//              |            productid,grossweight_sum,grossweight_sum_groupbyorder,
//              |
//              |            sku_de,
//              |            cast(sum(sku_de)   over (partition by orderno order by sku_de asc) as decimal(16,6)) as sumaccumulationvalue_de,
//              |            isnull(lag(sku_de) over (partition by orderno order by sku_de desc)) as islast_de
//              |
//              |        from
//              |        skuDEDF
//              |        )
//              |)
//            """.stripMargin)
//        skuDEDF_Rule.createOrReplaceTempView("skuDEDF_Rule")
//        //TODO：测试SQL: select * from skuDEDF_Rule where   customer_id = '1100000656' ,然后选取 orderno 为Z300013824114
//
//
//
//    }
//
//
//
//}
