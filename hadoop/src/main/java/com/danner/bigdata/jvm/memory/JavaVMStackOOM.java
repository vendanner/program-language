package com.danner.bigdata.jvm.memory;

/**
 * VM Args: -Xss2M
 * 设置 虚拟机栈大小 2M，并且使用多线程
 * 每个线程创建都会消耗2M 内存，线程越来越多就会 OOM
 * 当线程数无法削减时，减少栈大小也可解决OOM
 */
public class JavaVMStackOOM {

    private void dontStop(){
        while (true){
            try {
                System.out.println(Thread.currentThread().getId());
                Thread.sleep(1*60*1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void stackLeakByThread(){
        while (true){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    dontStop();
                }
            });
            thread.start();
        }
    }

    public static void main(String[] args) {
        new JavaVMStackOOM().stackLeakByThread();
    }

}
