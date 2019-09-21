package com.song.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.song.entity.ProxyConfig;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 飞鸽快信工具
 * Created by 17060342 on 2019/8/2.
 */
@Service
public class FeiGeUtil {

    /**
     * log日志
     */
    private static final Logger logger = LoggerFactory.getLogger(FeiGeUtil.class);

    @Autowired
    private ProxyConfig proxyConfig;

    private FeiGeUtil(){
    }

    /**
     * 获取代理
     * @return
     */
    private HttpHost getProxy(){
        return proxyConfig.getEnabled() ? new HttpHost(proxyConfig.getHost(),proxyConfig.getPort()) : null;
    }

    /**
     * SECRET KEY
     */
    public static String SECRET_KEY = "0ad06eb1731ae535ba75ff5ead41e5f1";

    /**
     * 促销APP_KEY
     */
    public static String CUXIAO_APP_KEY = "d1ea4a1e120f17a88a4a20a5e1dac4bc";

    /**
     * 测试模板id
     * data: {
     'first': { 'value': '{{first}}','color': '#173177'},
     'keyword1': { 'value': '{{keyword1}}','color': '#173177'},
     'keyword2': { 'value': '{{keyword2}}','color': '#173177'},
     'keyword3': { 'value': '{{keyword3}}','color': '#173177'},
     'remark': { 'value': '{{remark}}','color': '#173177'},
     }
     */
    public static String TEST_TEMPLATE_ID = "5uZIvSm5GAdUR1X25HNpjuOp6jSiL88v4opn4a4GLa0";

    /**
     * 发送消息
     * @param template_id
     * @param appKey
     * @param data
     * @throws Exception
     */
    public void sendMsg(String template_id,String appKey,String data) throws Exception {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("secret", SECRET_KEY);
        paramsMap.put("app_key", appKey);
        paramsMap.put("template_id", template_id);
        paramsMap.put("data", JSON.parse(data));
        String url = "https://u.ifeige.cn/api/message/send";
        logger.info("飞鸽快信发送消息,params:[{}]",JSON.toJSONString(paramsMap));
        String responseContent = HttpClientUtil.getInstance().httpPost(url, JSON.toJSONString(paramsMap), getProxy(), true);
        logger.info("飞鸽快信发送消息结果,responseContent:[{}],",responseContent);
    }

    /**
     * 发送促销消息
     * @param content
     * @throws Exception
     */
    public void sendCuXiaoMsg(String content) throws Exception {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("secret", SECRET_KEY);
        paramsMap.put("app_key", CUXIAO_APP_KEY);
        paramsMap.put("template_id", TEST_TEMPLATE_ID);
        JSONObject json = JSON.parseObject("{'first':{'value':'','color':'#173177'},'keyword1':{'value':'2','color':'#173177'},'keyword2':{'value':'3','color':'#173177'},'keyword3':{'value':'4','color':'#173177'},'remark':{'value':'','color':'#173177'}}");
        json.getJSONObject("keyword1").put("value",content);
        json.getJSONObject("keyword2").put("value","风");
        json.getJSONObject("keyword3").put("value",DateUtil.getFormatCurDate());
        paramsMap.put("data", json);
        String url = "https://u.ifeige.cn/api/message/send";
        logger.info("飞鸽快信发送消息,params:[{}]",JSON.toJSONString(paramsMap));
        String responseContent = HttpClientUtil.getInstance().httpPost(url, JSON.toJSONString(paramsMap), getProxy(), true);
        logger.info("飞鸽快信发送消息结果,responseContent:[{}],",responseContent);
    }

}
