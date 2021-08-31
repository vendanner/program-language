package com.danner.springboot.app.ch04;

import com.danner.springboot.app.ch04.converter.MoneyReadConverter;
import com.danner.springboot.app.ch04.model.Coffee;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * MongoTemplate 操作 mongo
 * 启动 mongoDB
 *      docker run --name mongo -p 27017:27017 -v ~/data/mongo:/data/db \
 *      -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=123456 \
 *      -d mongo
 * 进入 mongo
 *      mongo -u root -p 123456
 */
@Slf4j
@SpringBootApplication
public class MongoDBApplication implements ApplicationRunner {

    @Autowired
    MongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(MongoDBApplication.class);
    }


    /**
     * 这里相当于是 override mongoCustomConversions 函数
     * 添加自己的 MongoCustomConversions
     * @return
     */
    @Bean
    public MongoCustomConversions mongoCustomConversions(){
        return new MongoCustomConversions(Arrays.asList(new MoneyReadConverter()));
    }

    /**
     * MongoTemplate
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Date date = new Date();
        Coffee coffee = Coffee.builder()
                .name("danner")
                .price(Money.of(CurrencyUnit.of("CNY"), 20.0))
                .createTime(date)
                .updateTime(date)
                .build();
        // 新建数据保存
        Coffee save = mongoTemplate.save(coffee);
        log.info("Coffee {}", save);

        // 查找；Criteria 构造 where条件，query 执行查找
        List<Coffee> coffees = mongoTemplate.find(
                Query.query(Criteria.where("name").is("danner")), Coffee.class);
        log.info("coffees sizes {}", coffees.size());
        coffees.forEach(c -> log.info("Coff {}", c));

        // 更新 where条件，update更新数据，Coffee 限定表
        // newCoffee price和updateTime 已被更改
        Thread.sleep(1000);
        UpdateResult updateResult = mongoTemplate.updateFirst(
                Query.query(Criteria.where("name").is("danner")),
                new Update().set("price", Money.ofMajor(CurrencyUnit.of("CNY"), 30))
                        .currentDate("updateTime"),
                Coffee.class);
        log.info("updateResult size {}", updateResult.getModifiedCount());
        Coffee newCoffee = mongoTemplate.findById(save.getId(), Coffee.class);
        log.info("newCoffee {}", newCoffee);
    }
}
