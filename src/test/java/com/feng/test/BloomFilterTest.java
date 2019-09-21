package com.feng.test;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 17060342 on 2019/9/20.
 * -Xms64m -Xmx64m -XX:+PrintHeapAtGC -XX:+HeapDumpOnOutOfMemoryError
 */
@RunWith(JUnit4.class)
public class BloomFilterTest {
    @Test
    public void test(){
        long start = System.currentTimeMillis();
        int capacity = 10000000;
        //capacity预计存放多少数据，fpp可以接受的误报率
        BloomFilter<Integer> filter = BloomFilter.create(
                Funnels.integerFunnel(), capacity, 0.01);
        for (int i = 0; i < capacity; i++) {
            filter.put(i);
        }
        System.out.println(filter.mightContain(capacity));
        long end = System.currentTimeMillis();
        System.out.println("执行时间：" + (end - start));
    }
}
