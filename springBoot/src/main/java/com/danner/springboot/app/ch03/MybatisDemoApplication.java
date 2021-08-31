package com.danner.springboot.app.ch03;


import com.danner.springboot.app.ch03.mapper.CoffeeMapper;
import com.danner.springboot.app.ch03.model.MyBatisCoffee;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 使用 Mybatis 操作数据库，注解的方式定义 SQL(mapper中)
 * service -> mapper -> model
 *
 * @MapperScan      扫描 package 下 mapper
 *
 */
@SpringBootApplication
@Slf4j
@MapperScan("com.danner.springboot.app.ch03.mapper")
public class MybatisDemoApplication implements ApplicationRunner {

    @Autowired
    private CoffeeMapper coffeeMapper;

    public static void main(String[] args) {
        SpringApplication.run(MybatisDemoApplication.class);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        MyBatisCoffee c = MyBatisCoffee.builder().name("espresso")
                .price(Money.of(CurrencyUnit.of("CNY"), 20.0)).build();
        int count = coffeeMapper.save(c);
        log.info("Save {} Coffee: {}", count, c);

        c = MyBatisCoffee.builder().name("latte")
                .price(Money.of(CurrencyUnit.of("CNY"), 25.0)).build();
        count = coffeeMapper.save(c);
        log.info("Save {} Coffee: {}", count, c);

        c = coffeeMapper.findById(c.getId());
        log.info("Find Coffee: {}", c);
    }
}
