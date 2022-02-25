package org.danner.bigdata.flink.udf;

public class Test {

    public static void main(String[] args) {
        String s = "abba";
        int size = s.length();

        for (int i=0;i<s.length()-1;i++) {
           for (int j=i+1; j<s.length();j++) {
               String sub = s.substring(i, j+1);
               System.out.println(sub);
               if (isSub(sub)) {
                 size ++;
                 System.out.println(true);
               }
           }
        }
        System.out.println(size);
    }
    public static boolean isSub(String s) {
        int size = s.length();
        char[] chars = s.toCharArray();
        for (int i=0; i<size/2; i++) {
            if (chars[i] != chars[size - 1 -i]) {
                return false;
            }
        }
        return true;
    }
}
