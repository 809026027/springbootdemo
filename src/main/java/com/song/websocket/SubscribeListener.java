package com.song.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.song.utils.RedisUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import javax.websocket.Session;

/**
 * 订阅监听类
 * Created by 17060342 on 2019/6/12.
 */
public class SubscribeListener implements MessageListener {

    /**
     * log
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SubscribeListener.class);

    private RedisUtil redisUtil;

    private Session session;

    /**
     * 订阅接收发布者的消息
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = StringEscapeUtils.unescapeJava(new String(message.getBody()));
        if(null!=session && session.isOpen()){
            try {
                JSONObject json = JSON.parseObject(msg.substring(1,msg.length() - 1));
                logger.debug("{}主题发布消息:[{}]给订阅者:{}",new String(pattern),msg,session.getId());
                synchronized(session) {
                    session.getBasicRemote().sendText(json.toJSONString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public RedisUtil getRedisUtil() {
        return redisUtil;
    }

    public void setRedisUtil(RedisUtil stringRedisTemplate) {
        this.redisUtil = redisUtil;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

}
