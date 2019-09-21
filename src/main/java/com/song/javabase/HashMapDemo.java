package com.song.javabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 17060342 on 2019/9/6.
 */
public class HashMapDemo {

    public static void main(String[] args){
        //jdk1.8以下会引起部分线程死循环，多试几次，不是必现，jdk1.8解决了该问题
        //testMyThread();

        //hashmap多线程hash冲突导致数据被覆盖，jdk1.8也有此问题
        testMyThread1();

        //concurrenthashmap多线程解决MyThread1的问题
        //testMyThread2();
    }

    public static void testMyThread(){
        for(int i = 1; i <= 10; i++){
            new Thread(new MyThread(i)).start();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(MyThread.atomicInteger);
        System.out.println(MyThread.map.size());
    }

    public static void testMyThread1(){
        for(int i = 1; i <= 10; i++){
            new Thread(new MyThread1(i)).start();
        }
        try {
            new Thread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(MyThread1.map.size());
    }

    public static void testMyThread2(){
        for(int i = 1; i <= 10; i++){
            new Thread(new MyThread2(i)).start();
        }
        try {
            new Thread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(MyThread2.map.size());
    }
}

class MyThread implements Runnable{

    public static Map<Integer,Integer> map = new HashMap<Integer,Integer>(1);
    public static AtomicInteger atomicInteger = new AtomicInteger(0);

    private int id;

    public MyThread(int id){
        this.id = id;
    }

    @Override
    public void run() {
        for(int i = 0; i < 100000; i++) {
            //由于atomicInteger取值和加值不在一步操作，多线程下不但有数据跳过导致丢失，还可能产生同值hash碰撞，导致resize时hashmap死循环
            map.put(atomicInteger.get(), atomicInteger.get());
            atomicInteger.incrementAndGet();

            //即使atomicInteger取值和加值在一步操作，多线程下不会导致死循环，但是会有数据跳过导致丢失，atomic并不是万能的
            //int index = atomicInteger.getAndIncrement();
            //map.put(index, index);

            //同步代码块解决上述问题
            //synchronized (atomicInteger){
                //map.put(atomicInteger.get(), atomicInteger.get());
                //atomicInteger.incrementAndGet();
            //}
        }
        System.out.println(id + "结束");
    }
}

class MyThread1 implements Runnable{

    public static Map<Integer,Integer> map = new HashMap<Integer, Integer>(1);
    public int index;

    private int id;

    public MyThread1(int id){
        this.id = id;
        this.index = (id - 1) * 10000;
    }

    @Override
    public void run() {
        int count = 0;
        //index分区间线程不干扰，最终hashmap的大小不够10w，说明有hash冲突导致数据被覆盖。
        for(int i = 0; i < 10000; i++) {
            map.put(index , index);
            index ++;
            count ++;
        }
        System.out.println(id + "结束，总共" + count +"次");
    }
}

class MyThread2 implements Runnable{

    public static Map<Integer,Integer> map = new ConcurrentHashMap<Integer, Integer>(1);
    public int index;

    private int id;

    public MyThread2(int id){
        this.id = id;
        this.index = (id - 1) * 10000;
    }

    @Override
    public void run() {
        int count = 0;
        //index分区间多线程不干扰，改成concurrentHashMap，不存在hash冲突数据被覆盖的问题
        for(int i = 0; i < 10000; i++) {
            map.put(index , index);
            index ++;
            count ++;
        }
        System.out.println(id + "结束，总共" + count +"次");
    }
}
