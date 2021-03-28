package com.danner.java.thread;

/**
 * thread join()，等线程执行完再执行
 *
 * @Created by danner
 * @Date 2021-03-02 10:32
 */
public class ThreadJoin {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行前");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("执行后");
            }
        });

        // 等待 thread 执行结束
        thread.start();
        thread.join();

        System.out.println("thread 已执行结束");
    }
}
