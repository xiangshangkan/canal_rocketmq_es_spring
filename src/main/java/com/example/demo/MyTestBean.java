package com.example.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: Xml方式加载bean
 */
public class MyTestBean {

    private String testStr = "testStr";

    public String getTestStr() {
        return testStr;
    }
    public void setTestStr(String testStr) {
        this.testStr = testStr;
    }

    public static void main(String[] args) {
        Integer h = 1;
        Integer some = null;
        System.out.println(h.equals(some));
    }

}
