package com.danner.springboot.app.ch03.model;

import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * 表 T_MENU 映射结构
 *  Table   name 重命名表名
 *  Entity  自动进行表的DDL操作
 */
@Entity
@Data
@Builder
@Table(name = "T_MENU")
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Coffee extends BaseEntity implements Serializable {

    private String name;

    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money price;
}
