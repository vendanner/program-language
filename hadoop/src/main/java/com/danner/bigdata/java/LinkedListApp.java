package com.danner.bigdata.java;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * linkedlist 探究
 */
public class LinkedListApp {
    public static void main(String[] args) {

        LinkedList<String> list = new LinkedList<>();
        list.add(1,"");
        list.remove(1);
        list.add("shanghai");
        list.add("hangzhou");
        list.add("beijing");
        list.add("shenzhen");
        list.add("guangzhou");
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.get("");
        linkedHashMap.put("","");
    }
}
