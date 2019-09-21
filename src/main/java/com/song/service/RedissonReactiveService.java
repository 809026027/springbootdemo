package com.song.service;

import org.redisson.api.*;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by feng on 2019/9/21.
 */
@Service("redissonReactiveService")
public class RedissonReactiveService {

    @Autowired
    private RedissonReactiveClient redissonReactiveClient;

    public void getredissonReactiveClient() throws IOException {
        Config config = redissonReactiveClient.getConfig();
        System.out.println(config.toJSON().toString());
    }

    /**
     * `
     * 获取字符串对象
     *
     * @param objectName
     * @return
     */
    public <T> RBucketReactive<T> getRBucket(String objectName) {
        RBucketReactive<T> bucket = redissonReactiveClient.getBucket(objectName);
        return bucket;
    }

    /**
     * 获取Map对象
     *
     * @param objectName
     * @return
     */
    public <K, V> RMapReactive<K, V> getRMap(String objectName) {
        RMapReactive<K, V> map = redissonReactiveClient.getMap(objectName);
        return map;
    }

    /**
     * 获取集合
     *
     * @param objectName
     * @return
     */
    public <V> RSetReactive<V> getRSet(String objectName) {
        RSetReactive<V> rSet = redissonReactiveClient.getSet(objectName);
        return rSet;
    }

    /**
     * 获取列表
     *
     * @param objectName
     * @return
     */
    public <V> RListReactive<V> getRList(String objectName) {
        RListReactive<V> rList = redissonReactiveClient.getList(objectName);
        return rList;
    }

    /**
     * 获取队列
     *
     * @param objectName
     * @return
     */
    public <V> RQueueReactive<V> getRQueue(String objectName) {
        RQueueReactive<V> rQueue = redissonReactiveClient.getQueue(objectName);
        return rQueue;
    }

    /**
     * 获取双端队列
     *
     * @param objectName
     * @return
     */
    public <V> RDequeReactive<V> getRDeque(String objectName) {
        RDequeReactive<V> rDeque = redissonReactiveClient.getDeque(objectName);
        return rDeque;
    }


    /**
     * 获取锁
     *
     * @param objectName
     * @return
     */
    public RLockReactive getRLock(String objectName) {
        RLockReactive rLock = redissonReactiveClient.getLock(objectName);
        return rLock;
    }

    /**
     * 获取读取锁
     *
     * @param objectName
     * @return
     */
    public RReadWriteLockReactive getRWLock(String objectName) {
        RReadWriteLockReactive rwlock = redissonReactiveClient.getReadWriteLock(objectName);
        return rwlock;
    }

    /**
     * 获取原子数
     *
     * @param objectName
     * @return
     */
    public RAtomicLongReactive getRAtomicLong(String objectName) {
        RAtomicLongReactive rAtomicLong = redissonReactiveClient.getAtomicLong(objectName);
        return rAtomicLong;
    }


    /**
     * 获取消息的Topic
     *
     * @param objectName
     * @return
     */
    public <M> RTopicReactive<M> getRTopic(String objectName) {
        RTopicReactive<M> rTopic = redissonReactiveClient.getTopic(objectName);
        return rTopic;
    }
}