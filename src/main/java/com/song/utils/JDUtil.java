package com.song.utils;

import com.alibaba.fastjson.JSON;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.song.entity.ProxyConfig;
import jd.union.open.promotion.byunionid.get.request.UnionOpenPromotionByunionidGetRequest;
import jd.union.open.promotion.byunionid.get.response.UnionOpenPromotionByunionidGetResponse;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 17060342 on 2019/7/16.
 */
@Service
public final class JDUtil {

    /**
     * log日志
     */
    private static final Logger logger = LoggerFactory.getLogger(JDUtil.class);

    /**
     * 京东联盟appkey
     * https://union.jd.com/user
     */
    private static String APPKEY = "e2bc007a6133e180e22a4232e7751a81";

    /**
     * 京东联盟appsecret
     * https://union.jd.com/user
     */
    private static String APPSECRET = "d4777fb4002e4882b073f1a2de8f0480";

    /**
     * 京东联盟ID
     * 钱包账户128367027-72099222
     */
    private static long UNIONID = 1001742963;

    @Autowired
    private ProxyConfig proxyConfig;

    private JDUtil(){
    }

    /**
     * 获取代理
     * @return
     */
    private HttpHost getProxy(){
        return proxyConfig.getEnabled() ? new HttpHost(proxyConfig.getHost(),proxyConfig.getPort()) : null;
    }

    /**
     * 获取链接
     * @param sku 商品编码
     * @return
     */
    public static String getPromotionUrl(String sku){
        String SERVER_URL = "https://router.jd.com/api";

        JdClient client=new DefaultJdClient(SERVER_URL,"",APPKEY,APPSECRET);

        UnionOpenPromotionByunionidGetRequest request = new UnionOpenPromotionByunionidGetRequest();
        jd.union.open.promotion.byunionid.get.request.PromotionCodeReq promotionCodeReq = new jd.union.open.promotion.byunionid.get.request.PromotionCodeReq();
        promotionCodeReq.setMaterialId(String.format("https://item.jd.com/%s.html",sku));
        promotionCodeReq.setUnionId(UNIONID);

        request.setPromotionCodeReq(promotionCodeReq);
        try {
            UnionOpenPromotionByunionidGetResponse response = client.execute(request);
            System.out.println(JSON.toJSONString(response));
            return response.getData().getShortURL();
        } catch (JdException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 一键转链
     * @param url 初始推广链接
     * @return
     */
    public static String convertUrl(String url){
        String sugsUrl = url;
        try {
            String sku = "";
            sugsUrl = getPromotionUrl(sku);
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            return sugsUrl;
        }
    }
}
