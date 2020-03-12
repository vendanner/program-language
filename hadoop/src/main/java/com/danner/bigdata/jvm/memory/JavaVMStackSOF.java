package com.danner.bigdata.jvm.memory;

/**
 * VM Args: -Xss128k
 * 设置 虚拟机栈大小 128k
 */
public class JavaVMStackSOF {

    private int stackLength = 1;

    /**
     * 递归没有出口点，必定栈溢出
     */
    public void stackLeak(){
        stackLength ++;
        stackLeak();
    }
    public static void main(String[] args) throws Throwable{
        JavaVMStackSOF javaVMStackSOF = new JavaVMStackSOF();
        try {
            javaVMStackSOF.stackLeak();
        }catch (Throwable e){
            System.out.println("stack length: " + javaVMStackSOF.stackLength);
            throw e;
        }
    }
}
