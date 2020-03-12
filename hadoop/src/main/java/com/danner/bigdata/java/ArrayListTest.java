package com.danner.bigdata.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ArrayList 原理探究
 */
public class ArrayListTest {

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("beijing");
        System.out.println(list);
        list.add("shanghai");
        list.add("shanghai");
        list.add("guangzhou");
        list.add("shenzhen");
        list.add("hangzhou");
        list.remove("hangzhou");
        print(list);
        remove31(list, "shanghai");

    }
    private static void print(List<String> list){
        int size = list.size();
        for(int i = 0; i < size; i++){
            System.out.println("元素值：" + list.get(i));
        }
    }
    /*
     * 错误  循环中删除，size 变短导致数组越界
     */
    public static void remove11(List<String> list, String target){
        int size = list.size();
        for(int i = 0; i < size; i++){
            String item = list.get(i);
            if(target.equals(item)){
                list.remove(item);
            }
        }
        print(list);
    }
    /*
     * 错误；逻辑错误，虽然没有数组越界但还有一个 target 没有被删除
     */
    public static void remove12(List<String> list, String target){
        for(int i = 0; i < list.size(); i++){
            String item = list.get(i);
            if(target.equals(item)){
                list.remove(item);
            }
        }
        print(list);
    }
    /*
     * 正确 从后往前删除
     */
    public static void remove13(List<String> list, String target){
        int size = list.size();
        for(int i = size - 1; i >= 0; i--){
            String item = list.get(i);
            if(target.equals(item)){
                list.remove(item);
            }
        }
        print(list);
    }
    /*
     * 正确
     */
    public static void remove14(List<String> list, String target){
        for(int i = list.size() - 1; i >= 0; i--){
            String item = list.get(i);
            if(target.equals(item)){
                list.remove(item);
            }
        }
        print(list);
    }

    /*
     * 错误
     */
    public static void remove21(List<String> list, String target){
        for(String item : list){
            if(target.equals(item)){
                list.remove(item);
            }
        }
        print(list);
    }

    /*
     * 正确
     */
    public static void remove22(ArrayList<String> list, String target) {
        final CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<String>(list);
        for (String item : cowList) {
            if (item.equals(target)) {
                cowList.remove(item);
            }
        }
        print(cowList);
    }

    /*
     * 错误
     */
    public static void remove31(List<String> list, String target){
        Iterator<String> iter = list.iterator();
        while (iter.hasNext()) {
            String item = iter.next();
            if (item.equals(target)) {
                list.remove(item);
            }
        }
        print(list);
    }
    /*
     * 正确
     */
    public static void remove32(List<String> list, String target){
        Iterator<String> iter = list.iterator();
        while (iter.hasNext()) {
            String item = iter.next();
            if (item.equals(target)) {
                iter.remove();
            }
        }
        print(list);
    }

}
