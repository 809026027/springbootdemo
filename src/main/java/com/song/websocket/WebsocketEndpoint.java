package com.song.websocket;

import com.alibaba.fastjson.JSONObject;
import com.song.config.HttpSessionConfigurator;
import com.song.utils.*;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by 17060342 on 2019/6/12.
 */
@Component
@ServerEndpoint(value="/websocket/{topic}/{myname}",configurator = HttpSessionConfigurator.class)
public class WebsocketEndpoint {

    /**
     * log
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WebsocketEndpoint.class);

    /**
     * 因为@ServerEndpoint不支持注入，所以使用SpringUtils获取IOC实例
     */
    private RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);

    private RedisMessageListenerContainer redisMessageListenerContainer = SpringUtil.getBean(RedisMessageListenerContainer.class);

    //存放该服务器该ws的所有连接。用处：比如向所有连接该ws的用户发送通知消息。
    private static CopyOnWriteArraySet<WebsocketEndpoint> sessions = new CopyOnWriteArraySet<WebsocketEndpoint>();

    //存放该服务器该ws的所有连接。用处：比如向所有连接该ws的用户发送通知消息。
    private static ConcurrentHashMap<String,HttpSession> HttpSessonMap = new ConcurrentHashMap<String,HttpSession>();

    private Session session;

    private HttpSession httpSession;

    @OnOpen
    public void onOpen(Session session,EndpointConfig config,@PathParam("topic")String topic,@PathParam("myname")String myname){
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String hostName = httpSession.getAttribute("hostName").toString();
        String ip = httpSession.getAttribute("ip").toString();
        logger.info("java websocket(hostName:{},ip:{},topic:{},myname:{}):打开连接",hostName,ip,topic,myname);
        if(redisUtil.get("open"+ip) != null){
            if(session.isOpen()){
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        redisUtil.set("open"+ip,"1",3);
        this.session = session;
        sessions.add(this);
        SubscribeListener subscribeListener = new SubscribeListener();
        subscribeListener.setSession(session);
        subscribeListener.setRedisUtil(redisUtil);
        //设置订阅topic
        redisMessageListenerContainer.addMessageListener(subscribeListener, new ChannelTopic(topic));
    }

    @OnClose
    public void onClose(Session session){
        logger.info("java websocket(sessionId:{}):关闭连接",session.getId());
        sessions.remove(this);
        SubscribeListener subscribeListener = new SubscribeListener();
        subscribeListener.setSession(session);
        subscribeListener.setRedisUtil(redisUtil);
        redisMessageListenerContainer.removeMessageListener(subscribeListener);
    }

    @OnMessage
    public void onMessage(Session session,String message,@PathParam("topic")String topic,@PathParam("myname") String myname) throws IOException {
        String hostName = httpSession.getAttribute("hostName").toString();
        String ip = httpSession.getAttribute("ip").toString();
        Set<String> set = redisUtil.keys(hostName + "*");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("myname",myname);
        jsonObject.put("message",message);
        if(set != null && set.size() >= 5){
            String txt = message;
            String error = hostName + "您憋刷了！！！";
            try {
                error = Base64Utils.encodeToString(RSAUtil.getInstance().encrypt(RSAUtil.getInstance().getPublicKey(), error.getBytes()));
                txt = new String(RSAUtil.getInstance().decrypt(RSAUtil.getInstance().getPrivateKey(), Base64Utils.decodeFromString(message)));
            } catch (Exception e) {
                logger.error(e.getMessage() +";[{}]",message);
            }
            logger.info("hostName:{},ip:{},topic:{},myname:{}):收到消息:[{}]",hostName,ip,topic,myname,txt);
            if(session.isOpen() && redisUtil.incr(ip,1) >= 3){
                session.close();
                onClose(session);
                jsonObject.put("message",error);
                redisUtil.convertAndSend(topic, jsonObject.toJSONString());
                redisUtil.del(ip);
            }
            return;
        }
        redisUtil.set(hostName + System.currentTimeMillis(),"1",5);
        logger.debug("java websocket(sessionId:{},myname:{}):收到消息:[{}]",session.getId(),myname,message);
        redisUtil.convertAndSend(topic, jsonObject.toJSONString());
    }

    @OnError
    public void onError(Session session,Throwable error){
        logger.info("java websocket 出现错误,sessionId;{}",session.getId());
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
