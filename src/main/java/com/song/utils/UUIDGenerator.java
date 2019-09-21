package com.song.utils;

import java.util.UUID;

/**
 * Created by 17060342 on 2019/6/5.
 */
public class UUIDGenerator {
    public UUIDGenerator() {
    }
    /**
     * 获得一个唯一性UUID
     * @return String UUID
     */
    public static String getUUID(){
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
    }
}
