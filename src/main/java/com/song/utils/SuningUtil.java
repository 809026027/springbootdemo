package com.song.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suning.api.DefaultSuningClient;
import com.suning.api.entity.netalliance.*;
import com.suning.api.entity.union.UnionInfomationGetRequest;
import com.suning.api.entity.union.UnionInfomationGetResponse;
import com.suning.api.exception.SuningApiException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Created by 17060342 on 2019/7/16.
 */
public final class SuningUtil {

    /**
     * log日志
     */
    private static final Logger logger = LoggerFactory.getLogger(SuningUtil.class);

    private SuningUtil(){
    }

    /**
     * 获取高佣商品
     * @param pageIndex
     * @param size
     * @return
     */
    public static JSONArray getInverstmentCommodity(String pageIndex, String size){
        InverstmentcommodityQueryRequest request = new InverstmentcommodityQueryRequest();
        request.setCategoryId("C01");
        request.setCityCode("025");
        request.setPageIndex(pageIndex);
        request.setPicHeight("200");
        request.setPicWidth("200");
        request.setSize(size);
        //api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
        request.setCheckParam(true);
        String serverUrl = "https://open.suning.com/api/http/sopRequest";
        String appKey = "188ff38b1b6fddca3cc0b0ff7c6abe6b";
        String appSecret = "73571d574f1aa2af8f35d70474546b97";
        DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
        JSONArray commodityInfo = new JSONArray();
        try {
            InverstmentcommodityQueryResponse response = client.excute(request);
            System.out.println("返回json/xml格式数据 :" + response.getBody());
            JSONObject obj = JSON.parseObject(response.getBody());
            commodityInfo = obj.getJSONObject("sn_responseContent").getJSONObject("sn_body")
                    .getJSONObject("queryInverstmentcommodity").getJSONArray("commodityInfo");
        } catch (SuningApiException e) {
            e.printStackTrace();
        } finally {
            return commodityInfo;
        }
    }

    /**
     * 小编推荐商品查询接口
     * @param pageIndex
     * @param size
     * @return
     */
    public static JSONArray getRecommendCommodity(String pageIndex, String size){
        RecommendcommodityQueryRequest request = new RecommendcommodityQueryRequest();
        request.setCityCode("025");
        request.setPageIndex(pageIndex);
        request.setPicHeight("200");
        request.setPicWidth("200");
        request.setSize(size);
        //api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
        request.setCheckParam(true);
        String serverUrl = "https://open.suning.com/api/http/sopRequest";
        String appKey = "188ff38b1b6fddca3cc0b0ff7c6abe6b";
        String appSecret = "73571d574f1aa2af8f35d70474546b97";
        DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
        JSONArray commodityInfo = new JSONArray();
        try {
            RecommendcommodityQueryResponse response = client.excute(request);
            System.out.println("返回json/xml格式数据 :" + response.getBody());
            JSONObject obj = JSON.parseObject(response.getBody());
            commodityInfo = obj.getJSONObject("sn_responseContent").getJSONObject("sn_body")
                    .getJSONObject("queryRecommendcommodity").getJSONArray("commodityInfo");
        }
        catch (SuningApiException e) {
            e.printStackTrace();
        }finally {
            return commodityInfo;
        }
    }

    /**
     * 获取自定义推广链接接口
     * @param adBookId 推广位id
     * @return
     */
    public static JSONObject getCustomPromotionUrl(String adBookId){
        CustompromotionurlQueryRequest request = new CustompromotionurlQueryRequest();
        request.setAdBookId(adBookId);
        request.setVisitUrl("https://ju.suning.com");
        //api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
        request.setCheckParam(true);
        String serverUrl = "https://open.suning.com/api/http/sopRequest";
        String appKey = "188ff38b1b6fddca3cc0b0ff7c6abe6b";
        String appSecret = "73571d574f1aa2af8f35d70474546b97";
        DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
        JSONObject urlInfo = new JSONObject();
        try {
            CustompromotionurlQueryResponse response = client.excute(request);
            System.out.println("返回json/xml格式数据 :" + response.getBody());
            JSONObject obj = JSON.parseObject(response.getBody());
            urlInfo = obj.getJSONObject("sn_responseContent").getJSONObject("sn_body")
                    .getJSONObject("queryCustompromotionurl");
        }
        catch (SuningApiException e) {
            e.printStackTrace();
        }finally {
            return urlInfo;
        }
    }

    /**
     * 获取商品或店铺推广链接接口
     * @param adBookId 推广位id
     * @param commCode 商品编码 如果商品编码不为空，则生成商品推广链接，否则生成店铺推广链接
     * @param mertCode 商家编码 10位数字
     * @param urlType url类型 1:长链接 2:短链接
     * @return
     */
    public static JSONObject getStorePromotionUrl(String adBookId, String commCode, String mertCode, String urlType){
        StorepromotionurlQueryRequest request = new StorepromotionurlQueryRequest();
        request.setAdBookId(adBookId);
        request.setCommCode(commCode);
        request.setMertCode(mertCode);
        request.setUrlType(urlType);
        //api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
        request.setCheckParam(true);
        String serverUrl = "https://open.suning.com/api/http/sopRequest";
        String appKey = "188ff38b1b6fddca3cc0b0ff7c6abe6b";
        String appSecret = "73571d574f1aa2af8f35d70474546b97";
        DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
        JSONObject urlInfo = new JSONObject();
        try {
            StorepromotionurlQueryResponse response = client.excute(request);
            //System.out.println("返回json/xml格式数据 :" + response.getBody());
            JSONObject obj = JSON.parseObject(response.getBody());
            urlInfo = obj.getJSONObject("sn_responseContent").getJSONObject("sn_body")
                    .getJSONObject("queryStorepromotionurl");
        }
        catch (SuningApiException e) {
            e.printStackTrace();
        }finally {
            return urlInfo;
        }
    }

    /**
     * 获取优惠券商品
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param positionId 推广位ID
     * @return
     */
    public static JSONArray getCouponProduct(int pageNo, int pageSize, String positionId){
        CouponproductQueryRequest request = new CouponproductQueryRequest();
        request.setPageNo(pageNo);
        request.setPageSize(pageSize);
        request.setPositionId(positionId);
        //api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
        request.setCheckParam(true);
        String serverUrl = "https://open.suning.com/api/http/sopRequest";
        String appKey = "188ff38b1b6fddca3cc0b0ff7c6abe6b";
        String appSecret = "73571d574f1aa2af8f35d70474546b97";
        DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
        JSONArray commodityInfo = new JSONArray();
        try {
            CouponproductQueryResponse response = client.excute(request);
            //System.out.println("返回json/xml格式数据 :" + response.getBody());
            JSONObject obj = JSON.parseObject(response.getBody());
            commodityInfo = obj.getJSONObject("sn_responseContent").getJSONObject("sn_body")
                    .getJSONArray("queryCouponproduct");
        } catch (SuningApiException e) {
            e.printStackTrace();
        } finally {
            return commodityInfo;
        }
    }

    /**
     * 单个查询联盟商品信息
     * @param goodsCode
     * @return
     */
    public static JSONObject getUnionInfomation(String goodsCode){
        UnionInfomationGetRequest request = new UnionInfomationGetRequest();
        request.setGoodsCode(goodsCode);
        //api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
        request.setCheckParam(true);
        String serverUrl = "https://open.suning.com/api/http/sopRequest";
        String appKey = "188ff38b1b6fddca3cc0b0ff7c6abe6b";
        String appSecret = "73571d574f1aa2af8f35d70474546b97";
        DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
        JSONObject commodityInfo = new JSONObject();
        try {
            UnionInfomationGetResponse response = client.excute(request);
            System.out.println("返回json/xml格式数据 :" + response.getBody());
            JSONObject obj = JSON.parseObject(response.getBody());
            commodityInfo = obj.getJSONObject("sn_responseContent").getJSONObject("sn_body")
                    .getJSONObject("getUnionInfomation");
        }
        catch (SuningApiException e) {
            e.printStackTrace();
        }finally {
            return commodityInfo;
        }
    }

    /**
     * 商品推荐短链接
     * @param supplierCode 商户号
     * @param commodityCode 商品编码
     * @return
     */
    public static String getShortLink(String supplierCode,String commodityCode){
        ExtensionlinkGetRequest request = new ExtensionlinkGetRequest();
        request.setProductUrl("https://product.suning.com/"+supplierCode+"/"+commodityCode+".html?adtype=1");
        //request.setPromotionId("fff");
        //request.setQuanUrl("https://quan.suning.com/lqzx_recommend.do?activityId=201903190001000725&activitySecretKey=kwrK5exkwqreo3QzO3GL7TSX");
        //api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
        request.setCheckParam(true);
        String serverUrl = "https://open.suning.com/api/http/sopRequest";
        String appKey = "188ff38b1b6fddca3cc0b0ff7c6abe6b";
        String appSecret = "73571d574f1aa2af8f35d70474546b97";
        DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
        String shortLink = "";
        try {
            ExtensionlinkGetResponse  response = client.excute(request);
            System.out.println("返回json/xml格式数据 :" + response.getBody());
            JSONObject obj = JSON.parseObject(response.getBody());
            shortLink = obj.getJSONObject("sn_responseContent").getJSONObject("sn_body")
                    .getJSONObject("getExtensionlink").getString("shortLink");
        }
        catch (SuningApiException e) {
            e.printStackTrace();
        }finally {
            return shortLink;
        }
    }

    /**
     * 商品推荐短链接
     * @param supplierCode 商户号
     * @param commodityCode 商品编码
     * @return
     */
    public static String getShortLink1(String supplierCode,String commodityCode){
        ThirdpartypromotionGetRequest request = new ThirdpartypromotionGetRequest();
        request.setChannel("122");
        request.setOuterId(supplierCode + commodityCode);
        //request.setSubUser("abcde12345");
        //api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
        request.setCheckParam(true);
        String serverUrl = "https://open.suning.com/api/http/sopRequest";
        String appKey = "188ff38b1b6fddca3cc0b0ff7c6abe6b";
        String appSecret = "73571d574f1aa2af8f35d70474546b97";
        DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
        String shortLink = "";
        try {
            ThirdpartypromotionGetResponse response = client.excute(request);
            //System.out.println("返回json/xml格式数据 :" + response.getBody());
            JSONObject obj = JSON.parseObject(response.getBody());
            shortLink = obj.getJSONObject("sn_responseContent").getJSONObject("sn_body")
                    .getJSONObject("getThirdpartygetpromotion").getString("union");
        }
        catch (SuningApiException e) {
            e.printStackTrace();
        }finally {
            return shortLink;
        }
    }

    /**
     * 一键转链
     * @param url 初始推广链接
     * @return
     */
    public static String convertUrl(String url){
        String sugsUrl = url;
        try {
            if(url.contains("sugs.suning.com")){
                HttpResponse response = HttpClientUtil.getInstance().httpPostResponse(url,new HashMap<String,String>(), new HashMap<String,String>(), null,true);
                Header[] headers = response.getHeaders("Location");
                url = headers[0].getValue();
            }
            String[] params = url.split("/");
            if(url.contains("shop.m.suning.com")){
                if(url.contains("sale")){
                    String[] p = params[4].substring(0,params[4].indexOf(".htm")).split("_");
                    sugsUrl = "https://sugs.suning.com/" + SuningUtil.getShortLink1(StringUtil.fillZero(p[0],10), StringUtil.fillZero(p[1],18));
                }else if(url.contains("product")){
                    sugsUrl = "https://sugs.suning.com/" + SuningUtil.getShortLink1(StringUtil.fillZero(params[4],10),StringUtil.fillZero(params[5].substring(0,params[4].indexOf(".htm")),18));
                }
            } else if(url.contains("pin.suning.com")){
                Document detail = Jsoup.connect(url).get();
                Element script = detail.getElementsByTag("script").get(0);
                JSONObject json = JSON.parseObject(script.data().replaceAll("var sn_config = ","").replaceAll(";",""));
                sugsUrl = "https://sugs.suning.com/" + SuningUtil.getShortLink1(json.getJSONObject("activity").getString("vendorCode"),json.getJSONObject("activity").getString("itemCode"));
            } else if(url.contains("sumfs.suning.com")){
                String commodityCode = URLUtil.getParam(url,"commodityCode");
                String supplierCode = URLUtil.getParam(url,"supplierCode");
                sugsUrl = "https://sugs.suning.com/" + SuningUtil.getShortLink1(StringUtil.fillZero(supplierCode,10),StringUtil.fillZero(commodityCode,18));
            } else if(url.contains(".htm") && params[4].contains(".htm")){
                sugsUrl = "https://sugs.suning.com/" + SuningUtil.getShortLink1(StringUtil.fillZero(params[3],10),StringUtil.fillZero(params[4].substring(0,params[4].indexOf(".htm")),18));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            return sugsUrl;
        }
    }

    /**
     * 推荐红包产品
     * @return
     */
    public static String recommendCouponProduct(){
        StringBuffer recommendContent = new StringBuffer();
        try {
            JSONArray products = getCouponProduct(1, 10, "243034");
            JSONObject json = null;
            for(Object obj : products){
                json = (JSONObject)obj;
                recommendContent.append(json.getJSONObject("couponProduct").getString("commodityName")).append("\n");
                recommendContent.append(json.getJSONObject("couponProduct").getString("bounsStrength")).append("\n");
                recommendContent.append("原价：").append(json.getJSONObject("couponProduct").getString("price"));
                recommendContent.append(" 优惠价：").append(json.getJSONObject("couponProduct").getString("favorablePrice")).append("\n");
                recommendContent.append(json.getJSONObject("couponProduct").getString("couponPromotionShortLink")).append("\n\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            return recommendContent.toString();
        }
    }

    public static void main(String[] args){
        //System.out.println(JSON.toJSONString(getInverstmentCommodity("1", "10")));
        //System.out.println(JSON.toJSONString(getRecommendCommodity("1", "10")));
        //System.out.println(JSON.toJSONString(getCouponProduct(1, 10, "1")));
        //System.out.println(URLUtil.URLDecoderString(getCustomPromotionUrl("243034").getString("shortUrl")));
        //System.out.println(URLUtil.URLDecoderString(getStorePromotionUrl("243034","150020195","0070057321","2").getString("wapExtendUrl")));
        //System.out.println(JSON.toJSONString(getUnionInfomation("70055337")));
        //System.out.println(JSON.toJSONString(getShortLink("0070204412","11046979146")));
        //System.out.println(JSON.toJSONString(getShortLink1("0070204412","000000011046979146")));
        //System.out.println(convertUrl("https://sugs.suning.com/BnSrsc7b"));
        //System.out.println(recommendCouponProduct());

        System.out.println("https://sugs.suning.com/" + getShortLink1("0000000000","000000000783115315"));

    }
}
