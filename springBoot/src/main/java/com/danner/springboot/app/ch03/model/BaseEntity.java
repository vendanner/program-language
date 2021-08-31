package com.danner.springboot.app.ch03.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据表基类，定义公共字段
 *  Id：             定义表主键
 *  GeneratedValue  自动生成
 *  Column          列定义，updatable = false 不可修改
 *  CreationTimestamp   创建时间
 *  UpdateTimestamp     更新时间
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(updatable = false)
    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date updateTime;
}
