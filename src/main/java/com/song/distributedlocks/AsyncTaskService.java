package com.song.distributedlocks;

import com.song.service.RedissonReactiveService;
import com.song.service.RedissonService;
import com.song.utils.CookieUtil;
import com.song.utils.RedisUtil;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 线程执行任务类
 * Created by 17060342 on 2019/6/13.
 */
@Service
public class AsyncTaskService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedissonService redissonService;

    @Autowired
    private DistributedLockByZookeeper distributedLockByZookeeper;

    public static Random random = new Random();

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);

    @Async
    // 表明是异步方法
    // 无返回值
    public void executeAsyncTask(String msg) {
        System.out.println(Thread.currentThread().getName()+"开启新线程执行" + msg);
    }

    /**
     * redis基础分布式锁
     *
     * Redis分布式锁坑(大坑)---reids主从复制结构，主节点未同步自己设置的锁的时候挂了，其他任务在从节点获取到锁，导致锁失效
     * 解决方案有RedLock，实现原理非常复杂，也没有解决所有问题，可参考https://blog.csdn.net/chen_kkw/article/details/81433470
     * 最终解决，redis分布式锁没有完善的方案，建议使用zookeeper分布式锁
     *
     * @param i
     * @return
     * @throws InterruptedException
     */
    @Async
    public Future<String> distributedLockRedis(String taskId,int i) throws InterruptedException {
        try{
            boolean result = redisUtil.setnx(taskId,"task"+i,2);
            if(!result){
                return new AsyncResult<String>("fail:" + i);// Future接收返回值，这里是String类型，可以指明其他类型
            }

            //Redis分布式锁坑---启动分线程定时任务给锁重新设置超时时间，防止超时时间已到，当前任务未完成导致锁失效，其他任务拿到锁的bug
            //xxxx()未实现

            System.out.println("input is " + i);
            Thread.sleep(1000l);
            return new AsyncResult<String>("success:" + i);// Future接收返回值，这里是String类型，可以指明其他类型;
        } finally {
            //Redis分布式锁坑---未捕获异常或者不能捕获的异常，导致锁未释放，导致死锁
            if(("task"+i).equals(redisUtil.get(taskId))){
                //Redis分布式锁坑---自己锁自己解锁，防止finally执行时自己解了公共锁，第二个任务获取锁的同时，第三个任务也获取到锁，导致锁失效
                redisUtil.del(taskId);
            }
        }
    }

    /**
     * redission分布式锁
     *
     * @param i
     * @return
     * @throws InterruptedException
     */
    @Async
    public Future<String> distributedLockRedisson(String taskId,int i) throws InterruptedException, ExecutionException {
        RLock lock = redissonService.getRLock(taskId);
        try{
            // 1. 最常见的使用方法
            //lock.lock();
            // 2. 支持过期解锁功能,2秒钟以后自动解锁, 无需调用unlock方法手动解锁
            //lock.lock(2, TimeUnit.SECONDS);
            // 3. 尝试加锁，最多等待1秒，上锁以后10秒自动解锁
            boolean bs = lock.tryLock(0, 100, TimeUnit.SECONDS);
            if(!bs){
                return new AsyncResult<String>("fail:" + i);// Future接收返回值，这里是String类型，可以指明其他类型
            }
            System.out.println("input is " + i);
            Thread.sleep(5000l);
            return new AsyncResult<String>("success:" + i);// Future接收返回值，这里是String类型，可以指明其他类型;
        }catch (Exception e){
            logger.error("error",e);
            return new AsyncResult<String>("fail:" + i);// Future接收返回值，这里是String类型，可以指明其他类型
        }
        finally {
            //lock.tryLock会阻塞自旋获取锁，若主动解锁，相当于阻塞锁，若不主动解锁，其他锁在第一个锁结束后获取失败，锁在超时后解锁
            //lock.unlock();
        }
    }

    /**
     * zookeeper分布式锁
     *
     * @param i
     * @return
     * @throws InterruptedException
     */
    @Async
    public Future<String> distributedLockZK(String taskId,int i) throws InterruptedException {
        Future<String> future;
        Boolean flag;
        distributedLockByZookeeper.acquireDistributedLock("song");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag = distributedLockByZookeeper.releaseDistributedLock("song");
        if (flag) {
            future = new AsyncResult<String>("success:" + i);
        }else{
            future = new AsyncResult<String>("fail:" + i);
        }

        return future;
    }

    @Async
    public Future<String> async1() throws Exception {
        long sleep = random.nextInt(10000);
        logger.info("开始任务1，需耗时：" + sleep + "毫秒");
        Thread.sleep(sleep);
        logger.info("完成任务1");
        return new AsyncResult<String>("test1");
    }

    @Async
    public Future<String> async2() throws Exception {
        long sleep = random.nextInt(10000);
        logger.info("开始任务2，需耗时：" + sleep + "毫秒");
        Thread.sleep(sleep);
        logger.info("完成任务2");
        return new AsyncResult<String>("test2");
    }
}
