package com.song.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.song.entity.EmailBean;
import com.song.entity.Promotion;
import com.song.entity.ProxyConfig;
import com.song.mapper.PromotionMapper;
import com.song.service.ElasticService;
import com.song.utils.*;
import org.apache.http.HttpHost;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 爬取任务
 * Created by 17060342 on 2019/7/3.
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class CrawlTask {
    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(CrawlTask.class);

    @Autowired
    private ProxyConfig proxyConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmailBean emailBean;

    @Autowired
    private TaobaoUtil taobaoUtil;

    @Autowired
    private PromotionMapper promotionMapper;

    @Autowired
    private ElasticService elasticService;

    /**
     * 获取代理
     *
     * @return
     */
    private HttpHost getProxy() {
        return proxyConfig.getEnabled() ? new HttpHost(proxyConfig.getHost(), proxyConfig.getPort()) : null;
    }

    /**
     * 爬取商品
     */
    //@Scheduled(cron = "0 0 0/6 * * ?")
    public void crawlGoodsTask() {
        crawlGoods("手机");
    }

    /**
     * 爬取评论商品
     */
    public String crawlGoods(String param) {
        logger.info("crawlGoods请求开始:param:{}", param);
        String url = "https://tuijian.suning.com/recommend-portal/recommendv2/biz.jsonp?parameter=" + param + "&sceneIds=2-1&count=10";
        org.springframework.http.HttpHeaders requestHeaders = new org.springframework.http.HttpHeaders();

        org.springframework.http.HttpEntity<String> requestEntity = new org.springframework.http.HttpEntity<String>(null, requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        logger.info("crawlGoods请求结果:" + response.getBody());
        return response.getBody();
    }

    /**
     * 爬取评论商品
     */
    public List<Map<String, Object>> crawlGoodInfo(String param) {
        logger.info("crawlGoodInfo请求开始:param:{}", param);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject obj = JSON.parseObject(param);
        JSONArray sugGoods = obj.getJSONArray("sugGoods");
        for (Object sugGood : sugGoods) {
            JSONArray skus = ((JSONObject) sugGood).getJSONArray("skus");
            for (Object sku : skus) {
                map = new HashMap<String, Object>();
                map.put("sugGoodsCode", ((JSONObject) sku).getString("sugGoodsCode"));
                list.add(map);
            }
        }
        for (Map<String, Object> tempMap : list) {
            String sugGoodsCode = tempMap.get("sugGoodsCode").toString();
            String url = "https://product.suning.com/0000000000/" + sugGoodsCode.substring(6) + ".html";
            org.springframework.http.HttpHeaders requestHeaders = new org.springframework.http.HttpHeaders();

            org.springframework.http.HttpEntity<String> requestEntity = new org.springframework.http.HttpEntity<String>(null, requestHeaders);
            logger.info("getGoodInfo请求开始:url:{}", url);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            Pattern p = Pattern.compile("\\\"clusterId\\\":\\\"(.{8})\\\"");    //最终问题确定在这里
            Matcher matcher = p.matcher(response.getBody());
            if (matcher.find()) {
                logger.info(matcher.group(0) + "|" + matcher.group(1));
                tempMap.put("clusterId", matcher.group(1));
            }
        }
        logger.info("crawlGoodInfo请求结束");
        return list;
    }

    /**
     * 爬取评论
     */
    public void crawlComment(List<Map<String, Object>> list) {
        logger.info("crawlComment请求开始:param:{}", list);
        for (Map<String, Object> map : list) {
            String url = "https://review.suning.com/ajax/cluster_review_lists/general-"
                    + map.get("clusterId") + "-" + map.get("sugGoodsCode")
                    + "-0000000000-total-1-default-10-----reviewList.htm";
            org.springframework.http.HttpHeaders requestHeaders = new org.springframework.http.HttpHeaders();

            org.springframework.http.HttpEntity<String> requestEntity = new org.springframework.http.HttpEntity<String>(null, requestHeaders);
            logger.info("getComment请求开始:url:{}", url);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            Pattern p = Pattern.compile("reviewList\\((.*)\\)");    //最终问题确定在这里
            Matcher matcher = p.matcher(response.getBody());
            if (matcher.find()) {
                logger.info("getComment请求结果:" + matcher.group(1));
            }
        }
        logger.info("crawlComment请求结束");
    }


    /**
     * 0818团任务
     * http://www.0818tuan.com/list-1-0.html

    //@Scheduled(cron = "0 0 0/1 * * ?")
    //或直接指定时间间隔，例如：5秒5000
    @Scheduled(fixedRate=60000)
    public void tuan0818Task() {
        String url = "http://www.0818tuan.com/list-1-0.html";
        String responseContent = HttpClientUtil.getInstance().httpGet(url, new HashMap<String, String>(), getProxy(), true);
        Pattern p = Pattern.compile("<a[^>]*href=\\\"/xbhd[^>]*>.*?</a>");
        //Pattern p = Pattern.compile("<a href=\"/xbhd.*?</a>");
        Matcher matcher = p.matcher(responseContent);
        if (matcher.find()) {
            logger.info(matcher.group());
        }
    }
     */

    /**
     * 0818团任务
     * http://www.0818tuan.com/list-1-0.html
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    //或直接指定时间间隔，例如：5秒5000
    //@Scheduled(fixedRate=60000)
    public void tuan0818Task() {tuan818("http://www.0818tuan.com/list-1-0.html", false);
    }

    /**
     * 0818团
     *
     * @param url
     */
    private void tuan818(String url, boolean isEmail) {
        List<String> keyList = Arrays.asList("京东,JD,jd,京豆,京东闪付,苏宁,suning,sn,云钻,易付宝,天猫,淘宝,聚划算,淘金币,taobao,支付宝,拼多多,拼夕夕,pdd,PDD,云闪付,微信,美团,滴滴,移动,话费,江苏,南京,地铁,荣耀,小米,招商银行,招行,工商银行,工行,建设银行,建行,中国银行,中行,苏宁银行,无门槛券,无敌券,秒杀,折,券,抢".split(","));
        //List<String> keyList = Arrays.asList("苏宁,suning,sn,云钻,易付宝".split(","));
        try {
            Connection conn = Jsoup.connect(url).timeout(30000);
            //conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            //conn.header("Accept-Encoding", "gzip, deflate, sdch");
            //conn.header("Accept-Language", "zh-CN,zh;q=0.8");
            //conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            Document doc = conn.get();
            Element redtag = doc.getElementById("redtag");
            StringBuffer sb = new StringBuffer();
            CrawlTask crawlTask = this;
            for (Element a : redtag.getElementsByTag("a")) {
                ThreadPoolUtil.execute(new Runnable() {
                    @Override
                    public void run(){
                        boolean isKey = false;
                        for (String key : keyList) {
                            if (a.ownText().contains(key)) {
                                isKey = true;
                                break;
                            }
                        }
                        if (isKey && a.attr("href").contains("xbhd")) {
                            String detailHtml = "";
                            String detailStr = "";
                            try {
                                detailHtml = crawlTask.tuan818DetailHtml("http://www.0818tuan.com" + a.attr("href"));
                                detailStr = crawlTask.tuan818Detail("http://www.0818tuan.com" + a.attr("href"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Promotion promotion = new Promotion(a.ownText(), detailHtml);
                            promotionMapper.addPromotion(promotion);
                            elasticService.addPromotion(promotion.getId() + "", a.ownText(), detailHtml);
                            sb.append("\n").append(a.ownText()).append("\n").append(detailStr).append("\n");
                        }
                    }
                });
            }
            if (isEmail) {
                EmailUtil.sendEmail("这是一封测试邮件", new String[]{"songfeng_123@126.com", "sunshuwen@jskerun.com.cn"}, null, sb.toString().replaceAll("\n", "<br>"), null, emailBean);
            }
        } catch (Throwable e) {
            logger.error("CrawlTask.tuan818:{}",e);
        }
    }

    /**
     * @param url
     * @return
     */
    public String tuan818Detail(String url) throws IOException {
        StringBuffer sb = new StringBuffer();
        try {
            Document detail = Jsoup.connect(url).get();
            Element contentEle = detail.getElementsByClass("post-content").get(0);
            for (Element aDetail : contentEle.getElementsByTag("a")) {
                String aHref = aDetail.attr("href");
                if (aHref.contains("m.0818tuan.com") && aHref.contains("visitUrl=")) {
                    aHref = URLUtil.URLDecoderString(aHref.substring(aHref.indexOf("visitUrl=")));
                }
                if (aHref.contains(".suning.com")) {
                    if (aHref.contains("product.") || aHref.contains("sugs.") || aHref.contains("shop.")
                            || aHref.contains("pin.") || aHref.contains("sumfs.")) {
                        aHref = SuningUtil.convertUrl(aHref);
                    }
                }
                if (aHref.contains(".taobao.com")) {
                    aHref = taobaoUtil.convertUrl(aHref);
                }
                if(aHref != null) {
                    aDetail.text("|" + aHref + "|");
                }
            }
            Elements pre = contentEle.getElementsContainingOwnText("上一篇");
            Elements next = contentEle.getElementsContainingOwnText("下一篇");
            if (pre.size() > 0) {
                contentEle.getElementsContainingOwnText("上一篇").get(0).nextElementSibling().nextElementSibling().remove();
                contentEle.getElementsContainingOwnText("上一篇").get(0).nextElementSibling().remove();
                contentEle.getElementsContainingOwnText("上一篇").get(0).remove();
            } else if (next.size() > 0) {
                contentEle.getElementsContainingOwnText("下一篇").get(0).nextElementSibling().remove();
                contentEle.getElementsContainingOwnText("下一篇").get(0).remove();
            }
            String[] params = contentEle.text().split(" ");
            for (String param : params) {
                if (param.length() <= 10) {
                    sb.append(param + " ");
                } else {
                    sb.append(param + "\n");
                }
            }
            sb.append("\n");
            System.out.print(sb.toString().replaceAll("\\|", "\n"));
            return sb.toString().replaceAll("\\|", "\n");
        } catch (Throwable e) {
            logger.error("CrawlTask.tuan818Detail:{}", e);
            return sb.toString();
        }
    }

    /**
     * @param url
     * @return
     */
    public String tuan818DetailHtml(String url) throws IOException {
        try {
            Document detail = Jsoup.connect(url).get();
            Element contentEle = detail.getElementsByClass("post-content").get(0).getElementsByTag("p").first();
            for (Element aDetail : contentEle.getElementsByTag("a")) {
                String aHref = aDetail.attr("href");
                if (aHref.contains("m.0818tuan.com") && aHref.contains("visitUrl=")) {
                    aHref = URLUtil.URLDecoderString(aHref.substring(aHref.indexOf("visitUrl=")));
                }
                if (aHref.contains(".suning.com")) {
                    if (aHref.contains("product.") || aHref.contains("sugs.") || aHref.contains("shop.")
                            || aHref.contains("pin.") || aHref.contains("sumfs.")) {
                        aHref = SuningUtil.convertUrl(aHref);
                    }
                }
                if (aHref.contains(".taobao.com")) {
                    aHref = taobaoUtil.convertUrl(aHref);
                }
                if(aHref != null){
                    aDetail.text(aHref);
                    aDetail.attr("href", aHref);
                }
            }
            Elements pre = contentEle.getElementsContainingOwnText("上一篇");
            Elements next = contentEle.getElementsContainingOwnText("下一篇");
            if (pre.size() > 0) {
                contentEle.getElementsContainingOwnText("上一篇").get(0).nextElementSibling().nextElementSibling().remove();
                contentEle.getElementsContainingOwnText("上一篇").get(0).nextElementSibling().remove();
                contentEle.getElementsContainingOwnText("上一篇").get(0).remove();
            } else if (next.size() > 0) {
                contentEle.getElementsContainingOwnText("下一篇").get(0).nextElementSibling().remove();
                contentEle.getElementsContainingOwnText("下一篇").get(0).remove();
            }
            return contentEle.html();
        } catch (Throwable e) {
            logger.error("CrawlTask.tuan818Detail", e);
            return "";
        }
    }
}


