package com.song.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.song.entity.ProxyConfig;
import com.song.utils.HttpClientUtil;
import com.song.utils.URLUtil;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 17060342 on 2019/6/3.
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class TBTask {

    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(TBTask.class);

    @Autowired
    private ProxyConfig proxyConfig;

    /**
     * 折淘客appkey
     * http://www.zhetaoke.com/user/open/open_appkey.aspx
     */
    private static String APPKEY = "e4b02f7352c24ce6abe221db484d1938";

    /**
     * 淘宝联盟QQ PID
     * mm_淘宝联盟账号的ID_媒体ID(siteId)_推广位的ID(adzone_id)
     */
    private static String QQ_PID = "mm_479540174_657000214_109216600380";

    /**
     * 淘宝联盟微信 PID
     */
    private static String WX_PID = "mm_479540174_657350141_109219100190";

    /**
     * 淘宝联盟APP PID
     */
    private static String APP_PID = "mm_479540174_657350201_109216800474";

    /**
     * 获取代理
     * @return
     */
    private HttpHost getProxy(){
        return proxyConfig.getEnabled() ? new HttpHost(proxyConfig.getHost(),proxyConfig.getPort()) : null;
    }

    private Map<String,String> login(String loginUrl, String cookie) {
        return new HashMap<String, String>();
    }

}
