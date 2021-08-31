package com.danner.springboot.app.ch09;

import com.sun.org.apache.xalan.internal.xsltc.trax.XSLTCSource;
import lombok.NoArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * desc:
 *
 * @author reese
 * @date 2021/08/18
 */
@NoArgsConstructor
public class MyApplicationRunner implements ApplicationRunner {

    private String name = "auto";

    public MyApplicationRunner(String name) {
        this.name = name;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("MyApplicationRunner for " + name);
    }
}
