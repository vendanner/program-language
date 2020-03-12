package com.danner.bigdata.jvm.memory;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * JVM 参数: -XX:MetaspaceSize=8m -XX:MaxMetaspaceSize=128m -XX:+PrintFlagsInitial
 * JDK 8 中 即时编译代码是在 Metaspace，指定 Metaspace 空间128m
 */
public class MetaspaceOOM {
    public static void main(String[] args) {
        int i = 0;

        try {
            for (;;) {
                i++;
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(OOMObject.class);
                enhancer.setUseCache(false);
                enhancer.setCallback(new MethodInterceptor() {
                    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                        return proxy.invokeSuper(obj, args);
                    }
                });
                enhancer.create();
            }
        } catch (Throwable e) {
            System.out.println("第" + i + "次时发生异常");
            e.printStackTrace();
        }
    }

    static class OOMObject {

    }
}
