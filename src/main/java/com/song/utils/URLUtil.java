package com.song.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 17060342 on 2019/7/16.
 */
public final class URLUtil {

    /**
     * log日志
     */
    private static final Logger logger = LoggerFactory.getLogger(URLUtil.class);

    private URLUtil(){

    }

    private static final String LOCAL_IP = "127.0.0.1";

    private static final String LOCAL_IPS = " 0:0:0:0:0:0:0:1";

    private static final String TAOBAO_URL = "http://ip.taobao.com/service/getIpInfo.php?ip=%s";

    private static final String SINA_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=%s";

    /**
     * url编码
     * @param str
     * @return
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * url解码
     * @param str
     * @return
     */
    public static String URLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * url取值
     * @param url
     * @param name
     * @return
     */
    public static String getParam(String url, String name) {
        List<String> paramList = getParamList(url, name);
        return paramList == null ? "" : paramList.get(0);
    }

    /**
     * url取值
     * @param url
     * @param name
     * @return
     */
    public static List<String> getParamList(String url, String name) {
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        String[] urlParts = url.split("\\?");
        if (urlParts.length > 1) {
            String query = urlParts[1];
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                String key = URLDecoderString(pair[0]);
                String value = "";
                if (pair.length > 1) {
                    value = URLDecoderString(pair[1]);
                }

                List<String> values = params.get(key);
                if (values == null) {
                    values = new ArrayList<String>();
                    params.put(key, values);
                }
                values.add(value);
            }
        }
        return params.get(name);
    }

    /**
     * 获取Ip地址
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (LOCAL_IP.equals(ipAddress) || LOCAL_IPS.equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                assert inet != null;
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照’,'分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     @param ip
     @return JSONObject
     */
    public static JSONObject getAddresses(String ip) {
        // 这里调用pconline的接口
        // http://ip.taobao.com/service/getIpInfo.php
        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
        logger.info("获取ip地址位置,ip:[{}]",ip);
        String returnStr = "";
        try {
            returnStr = HttpClientUtil.getInstance().httpGet(String.format(SINA_URL,ip), false);
            returnStr = HttpClientUtil.getInstance().httpGet(String.format(TAOBAO_URL,ip), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (returnStr != null) {
            // 处理返回的省市区信息
            logger.info("省市区信息：{}", returnStr);
            return JSON.parseObject(returnStr);
        }
        return null;
    }

    /**
     * GeoLite获取ip地址位置
     * @param ip
     * @return JSONObject
     */
    public static JSONObject getAddressByGeoLite(String ip){
        logger.info("获取ip地址位置,ip:[{}]",ip);
        JSONObject result = new JSONObject();
        try {
            // 创建 GeoLite2 数据库
            File database = ResourceUtils.getFile("classpath:geolite/GeoLite2-City.mmdb");
            // 读取数据库内容
            DatabaseReader reader = new DatabaseReader.Builder(database).build();
            InetAddress ipAddress = InetAddress.getByName(ip);
            // 获取查询结果
            CityResponse response = reader.city(ipAddress);
            result = (JSONObject)JSON.toJSON(response);
            logger.info("省市区信息：{}", result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (GeoIp2Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        //getAddresses("15.0.52.48");
        getAddressByGeoLite("15.0.52.48");
    }
}
