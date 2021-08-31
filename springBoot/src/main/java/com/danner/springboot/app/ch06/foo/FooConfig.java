package com.danner.springboot.app.ch06.foo;

import com.danner.springboot.app.ch06.context.TestBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


/**
 * 三个 testBean 开头的 bean
 */
@Configuration
@EnableAspectJAutoProxy
public class FooConfig {
    @Bean
    public TestBean testBeanX() {
        return new TestBean("foo");
    }

    @Bean
    public TestBean testBeanY() {
        return new TestBean("foo");
    }

    @Bean
    public FooAspect fooAspect() {
        return new FooAspect();
    }
}
