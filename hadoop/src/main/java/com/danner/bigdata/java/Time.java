package com.danner.bigdata.java;

/**
 * https://gitbook.cn/gitchat/column/undefined/topic/5d4d7e8c69004b174ccfffec
 */

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class Time {
  public static void main(String[] args) {
    // Sat Feb 15 16:09:25 CST 2020
    Date date = new Date();
    System.out.println(date);

    Calendar calendar = Calendar.getInstance();
    // Sat Feb 15 16:10:17 CST 2020
    Date time = calendar.getTime();
    System.out.println(time);


    // 以下都是线程安全 jdk8 出现
    // LocalDateTime class 包含 LocalDate 、LocalTime 属性
    LocalDateTime localDateTime;
    LocalDate localDate;
    LocalTime localTime;

    // 2020-02-15T16:17:41.195
    // 2020-02-15
    // 16:17:41.196
    System.out.println(LocalDateTime.now());
    System.out.println(LocalDate.now());
    System.out.println(LocalTime.now());
    // 获取本月的最后一天（JDK 8）
    LocalDate today = LocalDate.now();
    System.out.println(today.with(TemporalAdjusters.lastDayOfMonth()));

    // 时间戳
    System.out.println(Instant.now().toEpochMilli());
  }
}
