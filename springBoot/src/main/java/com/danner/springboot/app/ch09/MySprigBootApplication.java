package com.danner.springboot.app.ch09;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * desc:
 *
 * @author reese
 * @date 2021/08/18
 */
@SpringBootApplication
public class MySprigBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySprigBootApplication.class);
    }

    /**
     * 自己创建 MyApplicationRunner bean后，
     * MyAutoConfiguration 失效
     * @return
     */
    @Bean
    public MyApplicationRunner getMyApplicationRunner(){
        return new MyApplicationRunner("My");
    }

}
