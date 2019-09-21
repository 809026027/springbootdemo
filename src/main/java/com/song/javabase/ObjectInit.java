package com.song.javabase;

import com.song.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 17060342 on 2019/8/30.
 */
public class ObjectInit extends ObjectInitParent{

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);
    {
        a1 = 2;
        logger.info("代码块");
    }
    public ObjectInit(){
        a1 = 0;
        b1 = 0;
        logger.info("构造函数");
    }
    private int a1 = 1;
    static {
        a = 2;
        logger.info("静态代码块");
    }
    private int b1 = a1 + 1;


    public static void main(String[] args){
        ObjectInit init = new ObjectInit();
        System.out.println(init.a);
        System.out.println(init.b);
        System.out.println(init.a1);
        System.out.println(init.b1);
    }
}

class ObjectInitParent {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);
    {
        a1 = 20;
        logger.info("父代码块");
    }
    public ObjectInitParent(){
        a1 = -1;
        b1 = -1;
        logger.info("父构造函数");
    }
    private int a1 = 10;
    static {
        a = 20;
        logger.info("父静态代码块");
    }
    public static int a = 10;
    public static int b = a + 1;

    private int b1 = a1 + 1;
}


