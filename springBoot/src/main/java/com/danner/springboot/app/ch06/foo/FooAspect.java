package com.danner.springboot.app.ch06.foo;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class FooAspect {

    /**
     * AOP： 以 testBean 开头的 bean 执行后，调用此函数
     */
    @AfterReturning("bean(testBean*)")
    public void printAfter() {
        log.info("after hello()");
    }
}
