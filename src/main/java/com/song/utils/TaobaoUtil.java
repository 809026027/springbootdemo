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
 * Created by 17060342 on 2019/7/16.
 */
@Service
public final class TaobaoUtil {

    /**
     * log日志
     */
    private static final Logger logger = LoggerFactory.getLogger(TaobaoUtil.class);

    /**
     * 折淘客appkey
     * http://www.zhetaoke.com/user/open/open_appkey.aspx
     */
    private static String APPKEY = "e4b02f7352c24ce6abe221db484d1938";

    /**
     * 折淘客授权账号ID（sid）
     * 授权有效时间为30天，每月需要授权一次
     * [淘客账号]tb1941949_2012
     */
    private static String SID = "20150";

    /**
     * 淘宝联盟QQ PID
     * mm_淘宝联盟账号的ID_媒体ID(siteId)_推广位的ID(adzone_id)
     */
    public static String QQ_PID = "mm_479540174_657000214_109216600380";

    /**
     * 淘宝联盟微信 PID
     */
    public static String WX_PID = "mm_479540174_657350141_109219100190";

    /**
     * 淘宝联盟APP PID
     */
    public static String APP_PID = "mm_479540174_657350201_109216800474";

    @Autowired
    private ProxyConfig proxyConfig;

    private TaobaoUtil(){
    }

    /**
     * 获取代理
     * @return
     */
    private HttpHost getProxy(){
        return proxyConfig.getEnabled() ? new HttpHost(proxyConfig.getHost(),proxyConfig.getPort()) : null;
    }

    /**
     * 高佣转链 商品ID
     * @param pid
     * @param itemId 商品ID
     * @param signurl 1-http,2-https
     */
    public JSONObject gaoyongzhuanlian(String pid,String itemId,String signurl){
        logger.info("gaoyongzhuanlian请求开始:pid:{}",pid);
        JSONObject data = new JSONObject();
        try {
            String url = "https://api.zhetaoke.com:10001/api/open_gaoyongzhuanlian.ashx?appkey=%s&sid=%s&pid=%s&num_iid=%s&signurl=%s";
            Map<String, String> headMap = new HashMap<String,String>();
            String responseContent = HttpClientUtil.getInstance().httpGet(String.format(url,APPKEY,SID,pid,itemId,signurl), headMap, getProxy(),true);
            if(StringUtil.isNotNull(responseContent)){
                JSONObject obj = JSON.parseObject(responseContent);
                responseContent = HttpClientUtil.getInstance().httpGet(obj.getString("url"), headMap, getProxy(),true);
                if(StringUtil.isNotNull(responseContent) && JSON.parseObject(responseContent).getJSONObject("tbk_privilege_get_response") != null){
                    data = JSON.parseObject(responseContent).getJSONObject("tbk_privilege_get_response").getJSONObject("result").getJSONObject("data");
                }
            }
            logger.info("gaoyongzhuanlian请求结果:"+data);
        }catch (Throwable e){
            logger.error("TaobaoUtil.gaoyongzhuanlian:{}",e);
        }
        return data;
    }

    /**
     * 高佣转链 淘口令
     * @param pid
     * @param tkl 商品ID
     * @param signurl 1-http,2-https
     */
    public JSONObject gaoyongzhuanlian_tkl(String pid,String tkl,String signurl){
        logger.info("gaoyongzhuanlian_tkl请求开始:pid:{}",pid);
        String url = "https://api.zhetaoke.com:10001/api/open_gaoyongzhuanlian_tkl.ashx?appkey=%s&sid=%s&pid=%s&tkl=%s&signurl=%s";
        Map<String, String> headMap = new HashMap<String,String>();
        String responseContent = HttpClientUtil.getInstance().httpGet(String.format(url,APPKEY,SID,pid,tkl,signurl), headMap, getProxy(),true);
        JSONObject obj = JSON.parseObject(responseContent);
        responseContent = HttpClientUtil.getInstance().httpGet(obj.getString("url"), headMap, getProxy(),true);
        JSONObject data = new JSONObject();
        if(StringUtil.isNotNull(responseContent) && JSON.parseObject(responseContent).getJSONObject("tbk_privilege_get_response") != null){
            data = JSON.parseObject(responseContent).getJSONObject("tbk_privilege_get_response").getJSONObject("result").getJSONObject("data");
        }
        logger.info("gaoyongzhuanlian_tkl请求结果:"+data);
        return data;
    }

    /**
     * 高佣转链 淘口令
     * @param pid
     * @param text 口令弹框内容，长度大于5个字符，如：美美小编精心推荐
     * @param paramUrl 口令跳转目标页，如：https://uland.taobao.com/，必须以https开头，可以是二合一链接、长链接、短链接等各种淘宝高佣链接；
                            支持渠道备案链接。请注意，该参数需要进行Urlencode编码后传入。。
     * @param logo 口令弹框logoURL，如：https://img.alicdn.com/bao/uploaded/i2.jpg_200x200.jpg
     * @param signurl 1-http,2-https
     */
    public String tkl_create(String pid,String text,String paramUrl,String logo,String signurl){
        logger.info("tkl_create请求开始:pid:{},paramUrl:{}",pid,paramUrl);
        String url = "https://api.zhetaoke.com:10001/api/open_tkl_create.ashx?appkey=%s&sid=%s&text=%s&url=%s&logo=%s&signurl=%s";
        Map<String, String> headMap = new HashMap<String,String>();
        String responseContent = HttpClientUtil.getInstance().httpGet(String.format(url,APPKEY,SID,text,paramUrl,logo,signurl), headMap, getProxy(),true);
        JSONObject obj = JSON.parseObject(responseContent);
        responseContent = HttpClientUtil.getInstance().httpGet(obj.getString("url"), headMap, getProxy(),true);
        logger.info("tkl_create请求结果:"+responseContent);
        String data = JSON.parseObject(responseContent).getJSONObject("tbk_tpwd_create_response").getJSONObject("data").getString("model");
        logger.info("tkl_create请求结果:"+data);
        return data;
    }

    /**
     * 获取短连接
     * @param pid
     * @param paramUrl 口令跳转目标页，如：https://uland.taobao.com/，必须以https开头，可以是二合一链接、长链接、短链接等各种淘宝高佣链接；
     */
    public String getShorturl(String pid,String paramUrl){
        logger.info("getShorturl请求开始:pid:{},paramUrl:{}",pid,paramUrl);
        String url = "https://api.zhetaoke.com:10001/api/open_shorturl_taobao_get.ashx?appkey=%s&sid=%s&content=%s";
        String data = "";
        try
        {
            Map<String, String> headMap = new HashMap<String,String>();
            String responseContent = HttpClientUtil.getInstance().httpGet(String.format(url,APPKEY,SID,paramUrl), headMap, getProxy(),true);
            if(StringUtil.isNotNull(responseContent)){
                data = JSON.parseObject(responseContent).getString("shorturl");
            }
        }catch (Throwable e){
            logger.error("TaobaoUtil.getShorturl:{}",e);
        }
        logger.info("getShorturl请求结果:"+data);
        return data;
    }

    /**
     * 一键转链
     * @param url 初始推广链接
     * @return
     */
    public String convertUrl(String url){
        String sugsUrl = url;
        try {
            if(url.contains("uland.taobao.com")){
                sugsUrl = getShorturl(APP_PID,URLUtil.getURLEncoderString(url));
            } else if(url.contains("favorite.taobao.com")) {
                String itemid = URLUtil.getParam(url,"itemid");
                sugsUrl = gaoyongzhuanlian(APP_PID,itemid,"2").getString("coupon_click_url");
                sugsUrl = StringUtil.isNull(sugsUrl) ? url :getShorturl(APP_PID,sugsUrl);
            }
        }
        catch (Throwable e) {
            logger.error("TaobaoUtil.convertUrl:{}",e);
        }finally {
            return sugsUrl;
        }
    }
}
