package com.danner.springboot.app.ch09;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置
 *  没发现 MyApplicationRunner Bean，会自动创建一个
 *  若配置文件里 greeting.enabled=false，则此自动配置失效
 * @author reese
 * @date 2021/08/18
 */
@Configuration
@ConditionalOnClass(MyApplicationRunner.class)
public class MyAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(MyApplicationRunner.class)
    @ConditionalOnProperty(name = "greeting.enabled", havingValue = "true", matchIfMissing = true)
    public MyApplicationRunner greetingApplicationRunner() {
        return new MyApplicationRunner();
    }
}