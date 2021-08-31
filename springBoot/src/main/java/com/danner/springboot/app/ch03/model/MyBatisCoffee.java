package com.danner.springboot.app.ch03.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MyBatisCoffee {
    private Long id;
    private String name;
    private Money price;
    private Date createTime;
    private Date updateTime;
}
