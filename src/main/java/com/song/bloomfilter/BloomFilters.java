package com.song.bloomfilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import com.song.entity.User;
import com.song.service.UserService;
import com.song.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Pipeline;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by 17060342 on 2019/9/20.
 * 布隆过滤器
 * 1、只要返回数据不存在，则肯定不存在。
   2、返回数据存在，只能是大概率存在。
   3、不能清除其中的数据。
 */
@Component
public class BloomFilters {

    private static final int capacity = 1000000;
    private static final double fpp = 0.01;
    //布隆过滤器的键在Redis中的前缀 利用它可以统计过滤器对Redis的使用情况
    private String redisKeyPrefix = "bf:";
    private BloomFilter<String> filter;

    @Autowired
    private UserService userService;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 单机可用

    @PostConstruct
    public void init(){
        filter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")), capacity, fpp);
        List<User> userList = userService.findAllUsers();
        if (userList.isEmpty()){
            return;
        }
        for(User user : userList){
            filter.put(user.getName());
        }
    }
     */

    /**
     * redis bitmap方式
     */
    @PostConstruct
    public void init(){
        List<User> userList = userService.findAllUsers();
        if (userList.isEmpty()){
            return;
        }
        for(User user : userList){
            put("redis",user.getName());
        }
    }

    /**
     * 加入key
     * @param key
     */
    public void put(String key){
        filter.put(key);
    }

    /**
     * 判断是否存在
     * @param key
     * @return
     */
    public boolean mightContain(String key){
        return filter.mightContain(key);
    }

    //bit数组长度
    private long numBits = optimalNumOfBits(capacity, fpp);
    //hash函数数量
    private int numHashFunctions = optimalNumOfHashFunctions(capacity, numBits);

    //计算hash函数个数 方法来自guava
    private int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

    //计算bit数组长度 方法来自guava
    private long optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     * 判断keys是否存在于集合where中
     */
    public boolean isExist(String where, String key) {
        long[] indexs = getIndexs(key);
        boolean result = redisUtil.isExist(where,key,indexs);
        return result;
    }

    /**
     * 将key存入redis bitmap
     */
    private void put(String where, String key) {
        long[] indexs = getIndexs(key);
        redisUtil.setBit(where,key,indexs);
    }

    /**
     * 根据key获取bitmap下标 方法来自guava
     */
    private long[] getIndexs(String key) {
        long hash1 = hash(key);
        long hash2 = hash1 >>> 16;
        long[] result = new long[numHashFunctions];
        for (int i = 0; i < numHashFunctions; i++) {
            long combinedHash = hash1 + i * hash2;
            if (combinedHash < 0) {
                combinedHash = ~combinedHash;
            }
            result[i] = combinedHash % numBits;
        }
        return result;
    }

    /**
     * 获取一个hash值 方法来自guava
     */
    private long hash(String key) {
        Charset charset = Charset.forName("UTF-8");
        return Hashing.murmur3_128().hashObject(key, Funnels.stringFunnel(charset)).asLong();
    }
}
