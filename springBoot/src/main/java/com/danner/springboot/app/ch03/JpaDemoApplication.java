package com.danner.springboot.app.ch03;


import com.danner.springboot.app.ch03.model.Coffee;
import com.danner.springboot.app.ch03.model.CoffeeOrder;
import com.danner.springboot.app.ch03.model.OrderState;
import com.danner.springboot.app.ch03.repository.CoffeeOrderRepository;
import com.danner.springboot.app.ch03.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JPA 演示 coffee 和 coffeeOrder
 *  model：  映射数据库表结构
 *
 *
 * 自动创建
 *      // Money 最后映射成了 decimal(19,2)
 *      create table t_menu (
 *        id bigint not null,
 *         create_time timestamp,
 *         update_time timestamp,
 *         name varchar(255),
 *         price decimal(19,2),
 *         primary key (id)
 *     )
 *     // 这里没有 items 列，而是在下面创建了 t_order_coffee 表
 *      create table t_order (
 *        id bigint not null,
 *         create_time timestamp,
 *         update_time timestamp,
 *         customer varchar(255),
 *         state integer not null,
 *         primary key (id)
 *     )
 *     create table t_order_coffee (
 *        coffee_order_id bigint not null,
 *         items_id bigint not null
 *     )
 *
 *  插入数据
 *  查询数据
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@Slf4j
public class JpaDemoApplication implements ApplicationRunner {

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Autowired
    private CoffeeOrderRepository orderRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaDemoApplication.class);
    }


    /**
     * 插入数据
     * 查询数据
     * @param args
     * @throws Exception
     */
    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        initOrders();
        findOrders();
    }

    /**
     * 生成订单 ==> 往 coffee 和 order 表插入数据
     *  save：   插入数据
     */
    private void initOrders() {
        Coffee latte = Coffee.builder().name("latte")
                .price(Money.of(CurrencyUnit.of("CNY"), 30.0))
                .build();
        coffeeRepository.save(latte);
        log.info("Coffee: {}", latte);

        Coffee espresso = Coffee.builder().name("espresso")
                .price(Money.of(CurrencyUnit.of("CNY"), 20.0))
                .build();
        coffeeRepository.save(espresso);
        log.info("Coffee: {}", espresso);

        CoffeeOrder order = CoffeeOrder.builder()
                .customer("Li Lei")
                .items(Collections.singletonList(espresso))
                .state(OrderState.INIT)
                .build();
        orderRepository.save(order);
        log.info("Order: {}", order);

        order = CoffeeOrder.builder()
                .customer("Li Lei")
                .items(Arrays.asList(espresso, latte))
                .state(OrderState.INIT)
                .build();
        orderRepository.save(order);
        log.info("Order: {}", order);
    }

    /**
     *  查询表里数据
     *
     */
    private void findOrders() {
        coffeeRepository
                .findAll(Sort.by(Sort.Direction.DESC, "id"))
                .forEach(c -> log.info("Loading {}", c));

        List<CoffeeOrder> list = orderRepository.findTop3ByOrderByUpdateTimeDescIdAsc();
        log.info("findTop3ByOrderByUpdateTimeDescIdAsc: {}", getJoinedOrderId(list));

        list = orderRepository.findByCustomerOrderById("Li Lei");
        log.info("findByCustomerOrderById: {}", getJoinedOrderId(list));

        // 不开启事务会因为没Session而报LazyInitializationException
        list.forEach(o -> {
            log.info("Order {}", o.getId());
            o.getItems().forEach(i -> log.info("  Item {}", i));
        });

        list = orderRepository.findByItems_Name("latte");
        log.info("findByItems_Name: {}", getJoinedOrderId(list));
    }

    private String getJoinedOrderId(List<CoffeeOrder> list) {
        return list.stream().map(o -> o.getId().toString())
                .collect(Collectors.joining(","));
    }
}
