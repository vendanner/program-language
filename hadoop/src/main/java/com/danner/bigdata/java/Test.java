package com.danner.bigdata.java;

public class Test {
  public static int y = A.x + 1;
  public static void main(String[] args) {
    System.out.println(String.format("x=%d,y=%d", A.x, Test.y));
  }
}

class A {
  public static int x;
  static {
    x = Test.y + 1;
  }
}
