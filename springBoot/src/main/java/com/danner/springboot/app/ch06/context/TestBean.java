package com.danner.springboot.app.ch06.context;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class TestBean {
    private String context;
    // 输出 context，可以根据 context 值知道是哪个 Context 调用
    public void hello() {
        log.info("hello " + context);
    }
}
