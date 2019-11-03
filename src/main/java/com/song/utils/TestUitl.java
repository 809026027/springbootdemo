package com.song.utils;

import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 17060342 on 2019/6/27.
 */
public class TestUitl {
    public static void main(String[] args){

        //String str = "{\\\"myname\\\":\\\"feng\\\",\\\"message\\\":\\\"111\\\"}";
        //ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>(100);
        System.out.println("start");
        for(int i = 0; i< 100; i++){
            queue.add(i+"");
        }
        System.out.println("end");
    }
}
