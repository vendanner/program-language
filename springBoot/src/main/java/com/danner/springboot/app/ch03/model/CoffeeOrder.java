package com.danner.springboot.app.ch03.model;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 表 T_ORDER 结构
 *  Column(nullable = false)    不为空
 *  JoinTable                   外链，不生成实际列，会生成新的表，通过 id 关联
 */

@Entity
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "T_ORDER")
public class CoffeeOrder extends BaseEntity implements Serializable {

    private String customer;

    @ManyToMany
    @JoinTable(name = "T_ORDER_COFFEE")
    @OrderBy("id")
    private List<Coffee> items;

    @Enumerated
    @Column(nullable = false)
    private OrderState state;
}
