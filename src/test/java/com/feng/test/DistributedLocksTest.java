package com.feng.test;

import com.song.configuration.Entry;
import com.song.distributedlocks.AsyncTaskService;
import com.song.distributedlocks.DistributedLockByZookeeper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁demo
 * Created by 17060342 on 2019/6/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Entry.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class DistributedLocksTest {
    /**
     * log日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DistributedLocksTest.class);

    @Resource
    private AsyncTaskService asyncTaskService;

    @Test
    public void testReids() {

        for (int i = 0; i < 10; i++) {
            try {
                Future<String> future = asyncTaskService.distributedLockRedis("test", i);
                logger.info("线程{}返回{}", i, future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testRedisson() {

        for (int i = 0; i < 10; i++) {
            try {
                Future<String> future = asyncTaskService.distributedLockRedisson("test", i);
                logger.info("线程{}返回{}", i, future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testZK() {
        for (int i = 0; i < 10; i++) {
            try {
                Future<String> future = asyncTaskService.distributedLockZK("test", i);
                logger.info("线程{}返回{}", i, future.get());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testAsync() {
        try {
            logger.info("开始总任务");
            long time = System.currentTimeMillis();
            Future<String> future1 = asyncTaskService.async1();
            Future<String> future2 = asyncTaskService.async2();
            try {
                String str1 = future1.get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String str2 = future2.get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("总任务结束，耗时：" + (System.currentTimeMillis() - time) + "毫秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
