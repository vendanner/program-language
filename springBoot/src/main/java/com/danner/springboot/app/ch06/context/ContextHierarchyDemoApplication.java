package com.danner.springboot.app.ch06.context;

import com.danner.springboot.app.ch06.foo.FooConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Context 和AOP 关系
 * 		每个 Context 的 aop 增强只对本 Context 的 bean 生效
 *		切面配置成通用，父子Context 都要开启增强
 */
@SpringBootApplication
@Slf4j
public class ContextHierarchyDemoApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(ContextHierarchyDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// fooContext 是 barContext 的 父 Context
		ApplicationContext fooContext = new AnnotationConfigApplicationContext(FooConfig.class);
		ClassPathXmlApplicationContext barContext = new ClassPathXmlApplicationContext(
				new String[] {"applicationContext.xml"}, fooContext);
		// bean被aop增强了，会调用 after方法
		TestBean bean = fooContext.getBean("testBeanX", TestBean.class);
		bean.hello();

		log.info("=============");

		// 对于 barContext 来说，testBeanX 是通过xml创建
		// 而 testBeanY 是从父 Context fooContext 继承过来
		bean = barContext.getBean("testBeanX", TestBean.class);
		bean.hello();
		// 继承过来的testBeanY 也是被增强后的，也会调用 after方法
		bean = barContext.getBean("testBeanY", TestBean.class);
		bean.hello();
	}
}
