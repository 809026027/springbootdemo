package com.song.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.song.entity.ProxyConfig;
import com.song.utils.HttpClientUtil;
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
//@Component
//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
public class SNTask {

    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(SNTask.class);

    @Autowired
    private ProxyConfig proxyConfig;

    @Autowired
    private RestTemplate restTemplate;

    private List<String> cookieList = new ArrayList<String>(){{
        add("ids_r_me=NjA3MzE2OTI4NV9CUk9XU0VSXzE1NjY4MzM4MzAzNzJfMTU2NjgzMzgzMDM3Ml8wXzVmZWFlYjRj%0D%0AYWFiNmJmNWVjMjgzOTE3NWUxMzQ0ZWJm%0D%0A; tradeMA=133; custno=6073169285; idsLoginUserIdLastTime=18261941949; ");
        //add("ids_r_me=NjA3OTkzODY3MV9CUk9XU0VSXzE1NjM4MDk1NzQ3NDJfMTU2MzgwOTU3NDc0Ml8wX2VmMDkyZjMy%0D%0AY2EzODUyOTM3ZWE5ZDUwNjA3ZmFjNDRk%0D%0A; tradeMA=111; custno=6079938671; idsLoginUserIdLastTime=18351889225; ");
    }};

    /**
     * 获取代理
     * @return
     */
    private HttpHost getProxy(){
        return proxyConfig.getEnabled() ? new HttpHost(proxyConfig.getHost(),proxyConfig.getPort()) : null;
    }

    //店铺签到任务
    //@Scheduled(cron = "0 0 0,6,10 * * ?")
    //或直接指定时间间隔，例如：5秒5000
    //@Scheduled(fixedRate=60000)
    public void signSldesTask() {
        String loginUrl = "https://passport.suning.com/ids/login?service=https%3A%2F%2Fsldes.suning.com%2Fsldes-web%2Fauth%3FtargetUrl%3Dhttps%253A%252F%252Fsldes.suning.com%252Fsldes-web%252Fm%252Fsign%252Fsign.do%253Fdt%253Dmmds_lgVVOn8gggQVqn8ggg1V_ngTggg1V%257En8ggg1V5nsgggoVGnJggg3VDn8ggglJjngoggg.JknWggg0Jkn8gggj3kn8gggL3knsgggrZkngVgggpZkn8gggVZkn8ggg_Wknsggg4WknVgggZWkng8ggg6cknsggg_cEnVgggScjn8ggghc6n8ggg7c6ngTggg7cXn8ggg7cYn8ggg7cGn8gggLcbnHgggEcHngJgggAWLn8ggg9W7n1cggR3WccgggEZscVgggLZNcg8gggGW4c2nggWn.cWggg8nqcVgggNnOc8gggPnIcn8gggSn7csgggtnvcVggg3c0c8gggycQc8ggglc3cg8ggg0WMn8ggg5WBn8ggg8ZGn8gggqZKn8gggn3zngRgggh3OnJgggO3OnsgggK3OnmrFj94SLFCKlyLCy2K9CkYSWC9FrS2FSYFYjWmNHcg8WKhngUntqgg8WmfkLfrflL4gncWZ3JV8smA_ggMnucgggmrm4L4m4LllmeC4yfkR4lKFymFR4mRFKmgJsg6cLnm4m4Lllmrm4ffkl4kKFFyyrmlrRL4lkFKfm4LllmRlKmLlymOrggggggOrggmF8sggZjg5gggnZjgxgggWZjgVgggZZEg8gggJZrgg8gggJZcn8gggJZ8n8gggJZ1nVggg3ZCnsgggnZingxgggEWKnJgggDWbn8gggBW6n8ggg-Wjn8ggg2WEng8ggguWkn8gggzWgc8gggqWnc8gggpWncVgggOWncg.3ggUWncVgggIWgcsgggLWEn8ggghWln8ggghWYng8ggghW-n8ggghW_n8ggghWOn8gggLWLnVgggHWCngsgggqW0n8ggg%257EWTnsgggBWon8gggnZxnVgggTZxngsgggpZxnVggg83xn8gggS3on8gggJJ0n8gggiJCng8gggtJLn8gggwJHn8gg44lf4FFk4_._036cc39d-caf8-4736-9ec9-3364e0eeba68_._%2526callback%253DsignCallBack%2526supplierCode%253D70150601%2526activityCode%253D385811429229477892%2526signType%253D%2526channel%253DPC%2526dfpToken%253DTHtwYQ16b207cd82bHdKYc468%2526_%253D1559619822475&loginTheme=wap_new&multipleActive=false";
        for(String cookie : cookieList) {
            Map<String, String> loginMap = login(loginUrl, cookie);
            //authId=si05639A543E8DA4AAF8E83AA744B9AC16; secureToken=564A59886951905D910298876966150A
            String[] supplierCodes = "70224934,70614692,30000043,30000068,30000095,30000205,30000213,30000244,30000257,30000266,30000439,30000482,30000611,30000756,30000781,30000806,30001278,30001350,30001729,30001791,30002032,30002121,70061202,70061584,70075676,70082840,70091364,70116350,70128691,70134686,70204867,70233612,70241720,70242829,70284003,70756359,70756698,70792840,30001228,30001675,70140539,30000008,30000016,30000023,30000037,30000063,30000213,30000329,30000430,30000485,30000489,30000543,30000643,30000649,30000682,30000733,30000774,30000803,30000806,30000815,30000816,30000952,30001010,30001174,30001175".split(",");
            for (String supplierCode : supplierCodes) {
                signSldes(loginMap.get("authId"), loginMap.get("secureToken"), supplierCode);
            }
        }
    }

    /**
     * 云钻乐园签到任务
     * https://sign.suning.com/sign-web/m/newsign/to/index.do
     * http://i.pptv.com/icenter/index?aid=
     */
    @Scheduled(cron = "0 0 7 * * ?")
    //或直接指定时间间隔，例如：5秒5000
    //@Scheduled(fixedRate=60000)
    public void doSignTask() {
        String loginUrl = "https://passport.suning.com/ids/login?service=https%3A%2F%2Fsign.suning.com%2Fsign-web%2Fauth%3FtargetUrl%3Dhttps%253A%252F%252Fsign.suning.com%252Fsign-web%252Fm%252Fggame%252Fmember%252FdoSign.do%253FdfpToken%253DTHybuI16b258cdc32bUH43bef%2526channelType%253D4&loginTheme=b2c&multipleActive=false";
        for(String cookie : cookieList) {
            Map<String, String> loginMap = login(loginUrl, cookie);
            doSign(loginMap.get("authId"), loginMap.get("secureToken"));
        }
    }

    //收集雨滴任务
    @Scheduled(cron = "0 0 0/1 * * ?")
    //或直接指定时间间隔，例如：5秒5000
    //@Scheduled(fixedRate=60000)
    public void collectDropletTask() {
        String loginUrl = "https://passport.suning.com/ids/login?service=https%3A%2F%2Fsign.suning.com%2Fsign-web%2Fauth%3FtargetUrl%3Dhttps%253A%252F%252Fsign.suning.com%252Fsign-web%252Fm%252Fggame%252Fdevelop%252FhomeInfo.do%253FfriendMemberId=%252F_=1559735233242";
        for(String cookie : cookieList) {
            Map<String, String> loginMap = login(loginUrl, cookie);
            collectDroplet(loginMap.get("authId"), loginMap.get("secureToken"));
        }
    }

    //云钻任务
    @Scheduled(cron = "0 0 7 * * ?")
    //或直接指定时间间隔，例如：5秒5000
    public void yunzuanTask() {
        String loginUrl = "https://passport.suning.com/ids/login?service=https%3A%2F%2Fesg.suning.com%2Fesg-web%2Fauth%3FtargetUrl%3Dhttps%253A%252F%252Fesg.suning.com%252Fesg-web%252F";
        for(String cookie : cookieList) {
            Map<String, String> loginMap = login(loginUrl, cookie);
            yunzuanTask(loginMap.get("authId"), loginMap.get("secureToken"));
        }
    }

    //任务云钻
    @Scheduled(cron = "0 30 7 * * ?")
    //或直接指定时间间隔，例如：5秒5000
    //@Scheduled(fixedRate=60000)
    public void dripTask() {
        String loginUrl = "https://passport.suning.com/ids/login?service=https%3A%2F%2Fsign.suning.com%2Fsign-web%2Fauth%3FtargetUrl%3Dhttps%253A%252F%252Fsign.suning.com%252Fsign-web%252Fm%252Fggame%252Fdrip.do%253F_=1559735233242";
        for(String cookie : cookieList) {
            Map<String, String> loginMap = login(loginUrl, cookie);
            drip(loginMap.get("authId"), loginMap.get("secureToken"));
        }
    }

    //收获任务
    @Scheduled(cron = "0 0 0/2 * * ?")
    //或直接指定时间间隔，例如：5秒5000
    //@Scheduled(fixedRate=60000)
    public void harvestTask() {
        String loginUrl = "https://passport.suning.com/ids/login?service=https%3A%2F%2Fsign.suning.com%2Fsign-web%2Fauth%3FtargetUrl%3Dhttps%253A%252F%252Fsign.suning.com%252Fsign-web%252Fm%252Fggame%252Fdevelop%252Fharvest.do%253F_=1559735233242";
        for(String cookie : cookieList) {
            Map<String, String> loginMap = login(loginUrl, cookie);
            harvest(loginMap.get("authId"), loginMap.get("secureToken"));
        }
    }

    //喂食任务
    @Scheduled(cron = "0 5 0/1 * * ?")
    //或直接指定时间间隔，例如：5秒5000
    //@Scheduled(fixedRate=60000)
    public void feedTask() {
        String loginUrl = "https://passport.suning.com/ids/login?service=https%3A%2F%2Fsign.suning.com%2Fsign-web%2Fauth%3FtargetUrl%3Dhttps%253A%252F%252Fsign.suning.com%252Fsign-web%252Fm%252Fggame%252Fdevelop%252Ffeed.do%253FfoodCode=nut&_=1560217113916";
        for(String cookie : cookieList) {
            Map<String, String> loginMap = login(loginUrl, cookie);
            //feed(loginMap.get("authId"),loginMap.get("secureToken"));
            feedRest(loginMap.get("authId"), loginMap.get("secureToken"));
        }
    }

    /**
     * 安抚任务
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    //或直接指定时间间隔，例如：5秒5000
    //@Scheduled(fixedRate=60000)
    public void appeaseTask() {
        String loginUrl = "https://passport.suning.com/ids/login?service=https%3A%2F%2Fsign.suning.com%2Fsign-web%2Fauth%3FtargetUrl%3Dhttps%253A%252F%252Fsign.suning.com%252Fsign-web%252Fm%252Fggame%252Fdevelop%252Fappease.do%253F_=1560217113916";
        for(String cookie : cookieList) {
            Map<String, String> loginMap = login(loginUrl, cookie);
            //feed(loginMap.get("authId"),loginMap.get("secureToken"));
            appease(loginMap.get("authId"), loginMap.get("secureToken"));
        }
    }

    /**
     * 偷云钻任务
     */
    @Scheduled(cron = "0 15 * * * ?")
    //或直接指定时间间隔，例如：5秒5000
    //@Scheduled(fixedRate=60000)
    public void stealTask() {
        String loginUrl = "https://passport.suning.com/ids/login?service=https%3A%2F%2Fsign.suning.com%2Fsign-web%2Fauth%3FtargetUrl%3Dhttps%253A%252F%252Fsign.suning.com%252Fsign-web%252Fm%252Fggame%252Fdevelop%252Fsocial%252FfriendList.do%253FpageNum=1&_=1560217113916";
        for(String cookie : cookieList) {
            Map<String, String> loginMap = login(loginUrl, cookie);
            //feed(loginMap.get("authId"),loginMap.get("secureToken"));
            steal(loginMap.get("authId"), loginMap.get("secureToken"));
        }
    }

    /**
     * 登录
     * @return
     */
    public Map<String,String> login(String loginUrl,String loginCookie){
        Map<String, String> loginMap = new HashMap<String,String>();
        Map<String, String> headMap = new HashMap<String,String>();
        headMap.put(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        headMap.put(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        headMap.put(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        headMap.put(HttpHeaders.CONNECTION, "keep-alive");
        headMap.put(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        headMap.put(HttpHeaders.HOST,"passport.suning.com");
        headMap.put("Upgrade-Insecure-Requests","1");
        headMap.put("Cookie",loginCookie);
        HttpResponse response = HttpClientUtil.getInstance().httpPostResponse(loginUrl,new HashMap<String,String>(), headMap, getProxy(),true);
        Header[] headers = response.getHeaders("Set-Cookie");
        logger.debug("Set-Cookie:"+ JSON.toJSONString(headers));
        String authId = "";
        String secureToken = "";
        String TGC = "";
        String ids_r_me = "";
        for(Header header : headers){
            if(header.getValue().contains("authId") || header.getValue().contains("secureToken")){
                String[] cookies = header.getValue().split(";");
                for(String cookie : cookies) {
                    if(cookie.contains("authId")){
                        String[] array = cookie.split("=");
                        authId = array[1].trim();
                        break;
                    }
                    if(cookie.contains("eppAuthId")){
                        String[] array = cookie.split("=");
                        authId = array[1].trim();
                        break;
                    }
                    if(cookie.contains("secureToken")){
                        String[] array = cookie.split("=");
                        secureToken = array[1].trim();
                        break;
                    }
                    if(cookie.contains("eppSecureToken")){
                        String[] array = cookie.split("=");
                        secureToken = array[1].trim();
                        break;
                    }
                    if(cookie.contains("TGC")){
                        String[] array = cookie.split("=");
                        TGC = array[1].trim();
                        break;
                    }
                    if(cookie.contains("ids_r_me")){
                        String[] array = cookie.split("=");
                        ids_r_me = array[1].trim();
                        break;
                    }
                }
            }
        }
        loginMap.put("authId",authId);
        loginMap.put("secureToken",secureToken);
        loginMap.put("TGC",TGC);
        loginMap.put("ids_r_me",ids_r_me);
        //logger.info("login请求结束:{}",JSON.toJSONString(loginMap));
        return loginMap;
    }

    /**
     * 店铺签到
     * @param authId
     * @param secureToken
     * @param supplierCode
     */
    public void signSldes(String authId,String secureToken,String supplierCode){
        logger.info("signSldes请求开始:authId:{},secureToken:{},supplierCode:{}",authId,secureToken,supplierCode);
        String signUrl = "https://sldes.suning.com/sldes-web/m/sign/sign.do?dt=mmds_lgVVOn8gggQVqn8ggg1V_ngTggg1V~n8ggg1V5nsgggoVGnJggg3VDn8ggglJjngoggg.JknWggg0Jkn8gggj3kn8gggL3knsgggrZkngVgggpZkn8gggVZkn8ggg_Wknsggg4WknVgggZWkng8ggg6cknsggg_cEnVgggScjn8ggghc6n8ggg7c6ngTggg7cXn8ggg7cYn8ggg7cGn8gggLcbnHgggEcHngJgggAWLn8ggg9W7n1cggR3WccgggEZscVgggLZNcg8gggGW4c2nggWn.cWggg8nqcVgggNnOc8gggPnIcn8gggSn7csgggtnvcVggg3c0c8gggycQc8ggglc3cg8ggg0WMn8ggg5WBn8ggg8ZGn8gggqZKn8gggn3zngRgggh3OnJgggO3OnsgggK3OnmrFj94SLFCKlyLCy2K9CkYSWC9FrS2FSYFYjWmNHcg8WKhngUntqgg8WmfkLfrflL4gncWZ3JV8smA_ggMnucgggmrm4L4m4LllmeC4yfkR4lKFymFR4mRFKmgJsg6cLnm4m4Lllmrm4ffkl4kKFFyyrmlrRL4lkFKfm4LllmRlKmLlymOrggggggOrggmF8sggZjg5gggnZjgxgggWZjgVgggZZEg8gggJZrgg8gggJZcn8gggJZ8n8gggJZ1nVggg3ZCnsgggnZingxgggEWKnJgggDWbn8gggBW6n8ggg-Wjn8ggg2WEng8ggguWkn8gggzWgc8gggqWnc8gggpWncVgggOWncg.3ggUWncVgggIWgcsgggLWEn8ggghWln8ggghWYng8ggghW-n8ggghW_n8ggghWOn8gggLWLnVgggHWCngsgggqW0n8ggg~WTnsgggBWon8gggnZxnVgggTZxngsgggpZxnVggg83xn8gggS3on8gggJJ0n8gggiJCng8gggtJLn8gggwJHn8gg44lf4FFk4_._036cc39d-caf8-4736-9ec9-3364e0eeba68_._&callback=signCallBack&activityCode=385811429229477892&signType=&channel=PC&dfpToken=THtwYQ16b207cd82bHdKYc468&_=1559619822475";
        Map<String, String> headMap = new HashMap<String,String>();
        headMap.put(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        headMap.put(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        headMap.put(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        headMap.put(HttpHeaders.CACHE_CONTROL, "max-age=0");
        headMap.put(HttpHeaders.CONNECTION, "keep-alive");
        headMap.put(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        headMap.put(HttpHeaders.HOST,"sldes.suning.com");
        headMap.put("Upgrade-Insecure-Requests","1");
        headMap.put("Cookie","_snvd=1551928678414LGgh5aE6T+S; _df_ud=13dc381c-9638-4d25-8956-19ea8fbf4384; hm_guid=02fe1c32-8643-4a8e-9dcb-e20ca2cd2dfb; cityId=9173; _device_session_id=p_3ea16061-9a65-4d93-bb19-d448f6f47dbf; _cp_dt=81ad985b-9618-4aef-8811-8688eb0b9109-79029; _ga=GA1.2.417107482.1554258543; snadt-id=%7C155425854321884532%7C%7C%7C%7C%7C; Hm_lvt_cb12e33a15345914e449a2ed82a2a216=1556096241; custno=6073169285; idsLoginUserIdLastTime=18261941949; sncnstr=ON50bFT4VCNUT%2FKz%2Fag10Q%3D%3D; tradeMA=133; smidV2=20190514162412077b675e1e1e40a0c054f32e870d1229005de06214c8c34c0; districtId=11375; SN_CITY=100_025_1000173_9173_11_11375_1_1; smhst=11124272076|0000000000a11125615766|0000000000a11048860940|0000000000a11087921955|0070144811a10915605220|0000000000a688589033|0000000000a10624736318|0070236733a10632770490|0070236733a10729485446|0070236733a826232219|0000000000a10991736272|0000000000a10903804877|0070061997a138817752|0070061997a11110565309|0070061997a10721444278|0070061997a11044430469|0000000000a10263045938|0000000000a10263045922|0000000000a10946938759|0000000000a164543430|0000000000; _snms=155909240202260969; payMA=59; route=a4d362f4c60353c80423ec260593425b; imAuthId=siB96142B9DD4B574CE2AAF221AA87A123; _gid=GA1.2.1867545892.1559618137; _snzwt=THtwYQ16b207cd82bHdKYc468; custLevel=161000000140; logonStatus=0; nick=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; nick2=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; hasTrustToEpp=6073169285; p_sessionId=gKIuHEDlXo3E06ku26KDL3WwtMp8bJwBJEGVJlzbQtBrOoIL23; sfp_sessionId=iz47sP6WIIKNj3JapmCo0xz3cRACeMdRxXw0xatVSmhdzbYBY3; EPPTGC=59*TGT57D377D48B7938965A95DB865CC9581ADC5F859C; eppAuthId=59%23si2B62783A25DAB35FAB923A395C854EE3; eppSecureToken=5ED4669BD6776CDAF2172F41221D5748; sessionId=oSv08tFfSmBOQ6mnt9DiYNPBWxMNDyk7KikLKyTqlUcAypBDh0; totalProdQty=5; _snadtp=5; mtisAbTest=B; mtisCartQty=5; cityCode=025; provinceCode=100; _snsr=union%7C14%7C%7C82d9da11-f574-4702-949e-2feea3c38cea%7C; _snma=1%7C155368611691865628%7C1553686116918%7C1559619783911%7C1559628198256%7C965%7C140; _snmp=155962819816867284; _snmb=155962819826996410%7C1559628198286%7C1559628198269%7C1; ");
        headMap.put("Cookie",headMap.get("Cookie") + "authId="+authId + "; secureToken="+ secureToken);

        String responseContent = HttpClientUtil.getInstance().httpGet(signUrl + "&supplierCode=" +supplierCode, headMap, getProxy(),true);
        logger.info("signSldes请求结果:"+responseContent);
    }

    /**
     * 收获雨滴
     * @param authId
     * @param secureToken
     */
    public void collectDroplet(String authId,String secureToken){
        logger.info("collectDroplet请求开始:authId:{},secureToken:{}",authId,secureToken);
        String homeUrl = "https://sign.suning.com/sign-web/m/ggame/develop/homeInfo.do?friendMemberId=&_=1563344670931";
        String stageUrl = "https://sign.suning.com/sign-web/m/ggame/develop/useStageProperty.do?stagePropertyCode=%s&_=1563431179257";
        String url = "https://sign.suning.com/sign-web/m/ggame/develop/collectDropletFragments.do?dropletCode=%s&_=1563344621147";
        Map<String, String> headMap = new HashMap<String,String>();
        headMap.put(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        headMap.put(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        headMap.put(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        headMap.put(HttpHeaders.CACHE_CONTROL, "max-age=0");
        headMap.put(HttpHeaders.CONNECTION, "keep-alive");
        headMap.put(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        headMap.put(HttpHeaders.HOST,"sign.suning.com");
        headMap.put("Upgrade-Insecure-Requests","1");
        headMap.put("Cookie","_snvd=1551928678414LGgh5aE6T+S; _df_ud=13dc381c-9638-4d25-8956-19ea8fbf4384; hm_guid=02fe1c32-8643-4a8e-9dcb-e20ca2cd2dfb; _device_session_id=p_3ea16061-9a65-4d93-bb19-d448f6f47dbf; _cp_dt=81ad985b-9618-4aef-8811-8688eb0b9109-79029; _ga=GA1.2.417107482.1554258543; snadt-id=%7C155425854321884532%7C%7C%7C%7C%7C; Hm_lvt_cb12e33a15345914e449a2ed82a2a216=1556096241; custno=6073169285; idsLoginUserIdLastTime=18261941949; sncnstr=ON50bFT4VCNUT%2FKz%2Fag10Q%3D%3D; tradeMA=133; smidV2=20190514162412077b675e1e1e40a0c054f32e870d1229005de06214c8c34c0; districtId=11375; SN_CITY=100_025_1000173_9173_11_11375_1_1; payMA=59; logonStatus=0; nick=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; nick2=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; _gid=GA1.2.58559421.1559724357; custLevel=161000000140; route=67d8bd4b0241ec29a6f02b856653c72c; cityId=9173; totalProdQty=5; _snadtp=5; _snms=15597335617553748; sfp_sessionId=RryFAF5P4UjtPmul2fCXCE4Q6DvcEA753JcMkxjsS4EJ1s1IZL; hasTrustToEpp=6073169285; _snzwt=THQt8q16b2aa1a0e04Z5ec230; _snsr=baidu%7Cbrand-jr%7C%7Ctitle%7C%25E8%258B%258F%25E5%25AE%2581%25E9%2587%2591%25E8%259E%258D*%3A*; EPPTGC=59*TGT92F7430DEA9A7DE809744887A473FF625B4317E4; eppAuthId=59%23si99D791DFF35FDA88DDBE7FDB05FF1D48; eppSecureToken=E72F4D8AE806061F585CBF8A4BB3F8A4; smhst=177548181|0000000000a11124272076|0000000000a11125615766|0000000000a11048860940|0000000000a11087921955|0070144811a10915605220|0000000000a688589033|0000000000a10624736318|0070236733a10632770490|0070236733a10729485446|0070236733a826232219|0000000000a10991736272|0000000000a10903804877|0070061997a138817752|0070061997a11110565309|0070061997a10721444278|0070061997a11044430469|0000000000a10263045938|0000000000a10263045922|0000000000a10946938759|0000000000; mtisAbTest=B; mtisCartQty=9; _snma=1%7C155368611691865628%7C1553686116918%7C1559790612660%7C1559793268078%7C1041%7C165; _snmp=155979326806631012; _snmb=155979326809359276%7C1559793268115%7C1559793268093%7C1; cityCode=025; provinceCode=100; ");
        headMap.put("Cookie",headMap.get("Cookie") + "authId="+authId + "; secureToken="+ secureToken);

        String responseContent = HttpClientUtil.getInstance().httpGet(homeUrl, headMap, getProxy(),true);
        logger.info("homeInfo请求结果:"+responseContent);
        //雨滴
        JSONArray droplets = JSON.parseObject(responseContent).getJSONObject("data").getJSONArray("dropletDTO");
        //保护卡
        JSONArray stages = JSON.parseObject(responseContent).getJSONObject("data").getJSONArray("stagePropertyDTO");
        if(stages != null){
            JSONObject stage = null;
            for(Object obj : stages) {
                stage = (JSONObject) obj;
                if ("1".equals(stage.getString("status"))) {
                    responseContent = HttpClientUtil.getInstance().httpGet(String.format(stageUrl, stage.getString("code")), headMap, getProxy(), true);
                    logger.info("useStageProperty请求结果:" + responseContent);
                }
            }
        }
        if(droplets != null){
            JSONObject droplet = null;
            for(Object obj : droplets){
                droplet = (JSONObject)obj;
                if(!"0".equals(droplet.getString("dropletCode"))){
                    responseContent = HttpClientUtil.getInstance().httpGet(String.format(url,droplet.getString("dropletCode")), headMap, getProxy(),true);
                    logger.info("collectDroplet请求结果:"+responseContent);
                }
            }
        }
        logger.info("collectDroplet请求结束");
    }

    /**
     * 云钻任务
     * @param authId
     * @param secureToken
     */
    public void yunzuanTask(String authId,String secureToken){
        logger.info("yunzuanTask请求开始:authId:{},secureToken:{}",authId,secureToken);
        String pitUrl = "https://sign.suning.com/sign-web/m/ggame/task/pit.do?_=1563362779349";
        //String url = "https://sign.suning.com/sign-web/m/ggame/task/hadUndertakeFrame.do?taskId=%s&taskType=3&_=1563362570270";
        String url = "https://sign.suning.com/sign-web/m/ggame/task/complete.do?channelId=%s&taskId=%s&_=1563412699338";
        Map<String, String> headMap = new HashMap<String,String>();
        headMap.put(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        headMap.put(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        headMap.put(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        headMap.put(HttpHeaders.CACHE_CONTROL, "max-age=0");
        headMap.put(HttpHeaders.CONNECTION, "keep-alive");
        headMap.put(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        headMap.put(HttpHeaders.HOST,"sign.suning.com");
        headMap.put("Upgrade-Insecure-Requests","1");
        headMap.put("Cookie","_snvd=1551928678414LGgh5aE6T+S; _df_ud=13dc381c-9638-4d25-8956-19ea8fbf4384; hm_guid=02fe1c32-8643-4a8e-9dcb-e20ca2cd2dfb; _device_session_id=p_3ea16061-9a65-4d93-bb19-d448f6f47dbf; _cp_dt=81ad985b-9618-4aef-8811-8688eb0b9109-79029; _ga=GA1.2.417107482.1554258543; snadt-id=%7C155425854321884532%7C%7C%7C%7C%7C; Hm_lvt_cb12e33a15345914e449a2ed82a2a216=1556096241; custno=6073169285; idsLoginUserIdLastTime=18261941949; sncnstr=ON50bFT4VCNUT%2FKz%2Fag10Q%3D%3D; tradeMA=133; smidV2=20190514162412077b675e1e1e40a0c054f32e870d1229005de06214c8c34c0; districtId=11375; SN_CITY=100_025_1000173_9173_11_11375_1_1; payMA=59; logonStatus=0; nick=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; nick2=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; _gid=GA1.2.58559421.1559724357; custLevel=161000000140; route=67d8bd4b0241ec29a6f02b856653c72c; cityId=9173; totalProdQty=5; _snadtp=5; _snms=15597335617553748; sfp_sessionId=RryFAF5P4UjtPmul2fCXCE4Q6DvcEA753JcMkxjsS4EJ1s1IZL; hasTrustToEpp=6073169285; _snzwt=THQt8q16b2aa1a0e04Z5ec230; _snsr=baidu%7Cbrand-jr%7C%7Ctitle%7C%25E8%258B%258F%25E5%25AE%2581%25E9%2587%2591%25E8%259E%258D*%3A*; EPPTGC=59*TGT92F7430DEA9A7DE809744887A473FF625B4317E4; eppAuthId=59%23si99D791DFF35FDA88DDBE7FDB05FF1D48; eppSecureToken=E72F4D8AE806061F585CBF8A4BB3F8A4; smhst=177548181|0000000000a11124272076|0000000000a11125615766|0000000000a11048860940|0000000000a11087921955|0070144811a10915605220|0000000000a688589033|0000000000a10624736318|0070236733a10632770490|0070236733a10729485446|0070236733a826232219|0000000000a10991736272|0000000000a10903804877|0070061997a138817752|0070061997a11110565309|0070061997a10721444278|0070061997a11044430469|0000000000a10263045938|0000000000a10263045922|0000000000a10946938759|0000000000; mtisAbTest=B; mtisCartQty=9; _snma=1%7C155368611691865628%7C1553686116918%7C1559790612660%7C1559793268078%7C1041%7C165; _snmp=155979326806631012; _snmb=155979326809359276%7C1559793268115%7C1559793268093%7C1; cityCode=025; provinceCode=100; ");
        headMap.put("Cookie",headMap.get("Cookie") + "authId="+authId + "; secureToken="+ secureToken);

        String responseContent = HttpClientUtil.getInstance().httpGet(pitUrl, headMap, getProxy(),true);
        logger.info("pit请求结果:"+responseContent);
        JSONArray taskList = JSON.parseObject(responseContent).getJSONObject("data").getJSONArray("taskList");
        String channelId = JSON.parseObject(responseContent).getJSONObject("data").getString("channelId");
        JSONObject task = null;
        for(Object obj : taskList){
            task = (JSONObject)obj;
            for(int i = 0; i < 3; i++){
                responseContent = HttpClientUtil.getInstance().httpGet(String.format(url,channelId,task.getString("taskId")), headMap, getProxy(),true);
                logger.info("storeTask请求结果:"+responseContent);
            }
        }
        logger.info("yunzuanTask请求结束");
    }

    /**
     * 任务云钻
     * @param authId
     * @param secureToken
     */
    public void drip(String authId,String secureToken){
        logger.info("drip请求开始:authId:{},secureToken:{}",authId,secureToken);
        String dripUrl = "https://sign.suning.com/sign-web/m/ggame/task/drip.do?_=1563353819675";
        String url = "https://sign.suning.com/sign-web/m/ggame/task/send.do?recordNo=%s&_=1563353734602";
        Map<String, String> headMap = new HashMap<String,String>();
        headMap.put(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        headMap.put(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        headMap.put(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        headMap.put(HttpHeaders.CACHE_CONTROL, "max-age=0");
        headMap.put(HttpHeaders.CONNECTION, "keep-alive");
        headMap.put(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        headMap.put(HttpHeaders.HOST,"sign.suning.com");
        headMap.put("Upgrade-Insecure-Requests","1");
        headMap.put("Cookie","_snvd=1551928678414LGgh5aE6T+S; _df_ud=13dc381c-9638-4d25-8956-19ea8fbf4384; hm_guid=02fe1c32-8643-4a8e-9dcb-e20ca2cd2dfb; _device_session_id=p_3ea16061-9a65-4d93-bb19-d448f6f47dbf; _cp_dt=81ad985b-9618-4aef-8811-8688eb0b9109-79029; _ga=GA1.2.417107482.1554258543; snadt-id=%7C155425854321884532%7C%7C%7C%7C%7C; Hm_lvt_cb12e33a15345914e449a2ed82a2a216=1556096241; custno=6073169285; idsLoginUserIdLastTime=18261941949; sncnstr=ON50bFT4VCNUT%2FKz%2Fag10Q%3D%3D; tradeMA=133; smidV2=20190514162412077b675e1e1e40a0c054f32e870d1229005de06214c8c34c0; districtId=11375; SN_CITY=100_025_1000173_9173_11_11375_1_1; payMA=59; logonStatus=0; nick=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; nick2=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; _gid=GA1.2.58559421.1559724357; custLevel=161000000140; route=67d8bd4b0241ec29a6f02b856653c72c; cityId=9173; totalProdQty=5; _snadtp=5; _snms=15597335617553748; sfp_sessionId=RryFAF5P4UjtPmul2fCXCE4Q6DvcEA753JcMkxjsS4EJ1s1IZL; hasTrustToEpp=6073169285; _snzwt=THQt8q16b2aa1a0e04Z5ec230; _snsr=baidu%7Cbrand-jr%7C%7Ctitle%7C%25E8%258B%258F%25E5%25AE%2581%25E9%2587%2591%25E8%259E%258D*%3A*; EPPTGC=59*TGT92F7430DEA9A7DE809744887A473FF625B4317E4; eppAuthId=59%23si99D791DFF35FDA88DDBE7FDB05FF1D48; eppSecureToken=E72F4D8AE806061F585CBF8A4BB3F8A4; smhst=177548181|0000000000a11124272076|0000000000a11125615766|0000000000a11048860940|0000000000a11087921955|0070144811a10915605220|0000000000a688589033|0000000000a10624736318|0070236733a10632770490|0070236733a10729485446|0070236733a826232219|0000000000a10991736272|0000000000a10903804877|0070061997a138817752|0070061997a11110565309|0070061997a10721444278|0070061997a11044430469|0000000000a10263045938|0000000000a10263045922|0000000000a10946938759|0000000000; mtisAbTest=B; mtisCartQty=9; _snma=1%7C155368611691865628%7C1553686116918%7C1559790612660%7C1559793268078%7C1041%7C165; _snmp=155979326806631012; _snmb=155979326809359276%7C1559793268115%7C1559793268093%7C1; cityCode=025; provinceCode=100; ");
        headMap.put("Cookie",headMap.get("Cookie") + "authId="+authId + "; secureToken="+ secureToken);

        String responseContent = HttpClientUtil.getInstance().httpGet(dripUrl, headMap, getProxy(),true);
        logger.info("drip请求结果:"+responseContent);
        JSONArray recordNos = JSON.parseObject(responseContent).getJSONArray("data");
        JSONObject recordNo = null;
        if(recordNos!= null && !recordNos.isEmpty()){
            for(Object obj : recordNos){
                recordNo = (JSONObject)obj;
                responseContent = HttpClientUtil.getInstance().httpGet(String.format(url,recordNo.getString("recordNo")), headMap, getProxy(),true);
                logger.info("sendTask请求结果:"+responseContent);
            }
        }
        logger.info("drip请求结束");
    }

    /**
     * 收获云钻
     * @param authId
     * @param secureToken
     */
    public void harvest(String authId,String secureToken){
        logger.info("harvest请求开始:authId:{},secureToken:{}",authId,secureToken);
        String url = "https://sign.suning.com/sign-web/m/ggame/develop/harvest.do?_=1559735233242";
        Map<String, String> headMap = new HashMap<String,String>();
        headMap.put(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        headMap.put(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        headMap.put(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        headMap.put(HttpHeaders.CACHE_CONTROL, "max-age=0");
        headMap.put(HttpHeaders.CONNECTION, "keep-alive");
        headMap.put(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        headMap.put(HttpHeaders.HOST,"sign.suning.com");
        headMap.put("Upgrade-Insecure-Requests","1");
        headMap.put("Cookie","_snvd=1551928678414LGgh5aE6T+S; _df_ud=13dc381c-9638-4d25-8956-19ea8fbf4384; hm_guid=02fe1c32-8643-4a8e-9dcb-e20ca2cd2dfb; _device_session_id=p_3ea16061-9a65-4d93-bb19-d448f6f47dbf; _cp_dt=81ad985b-9618-4aef-8811-8688eb0b9109-79029; _ga=GA1.2.417107482.1554258543; snadt-id=%7C155425854321884532%7C%7C%7C%7C%7C; Hm_lvt_cb12e33a15345914e449a2ed82a2a216=1556096241; custno=6073169285; idsLoginUserIdLastTime=18261941949; sncnstr=ON50bFT4VCNUT%2FKz%2Fag10Q%3D%3D; tradeMA=133; smidV2=20190514162412077b675e1e1e40a0c054f32e870d1229005de06214c8c34c0; districtId=11375; SN_CITY=100_025_1000173_9173_11_11375_1_1; payMA=59; logonStatus=0; nick=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; nick2=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; _gid=GA1.2.58559421.1559724357; custLevel=161000000140; route=67d8bd4b0241ec29a6f02b856653c72c; cityId=9173; totalProdQty=5; _snadtp=5; _snms=15597335617553748; sfp_sessionId=RryFAF5P4UjtPmul2fCXCE4Q6DvcEA753JcMkxjsS4EJ1s1IZL; hasTrustToEpp=6073169285; _snzwt=THQt8q16b2aa1a0e04Z5ec230; _snsr=baidu%7Cbrand-jr%7C%7Ctitle%7C%25E8%258B%258F%25E5%25AE%2581%25E9%2587%2591%25E8%259E%258D*%3A*; EPPTGC=59*TGT92F7430DEA9A7DE809744887A473FF625B4317E4; eppAuthId=59%23si99D791DFF35FDA88DDBE7FDB05FF1D48; eppSecureToken=E72F4D8AE806061F585CBF8A4BB3F8A4; smhst=177548181|0000000000a11124272076|0000000000a11125615766|0000000000a11048860940|0000000000a11087921955|0070144811a10915605220|0000000000a688589033|0000000000a10624736318|0070236733a10632770490|0070236733a10729485446|0070236733a826232219|0000000000a10991736272|0000000000a10903804877|0070061997a138817752|0070061997a11110565309|0070061997a10721444278|0070061997a11044430469|0000000000a10263045938|0000000000a10263045922|0000000000a10946938759|0000000000; mtisAbTest=B; mtisCartQty=9; _snma=1%7C155368611691865628%7C1553686116918%7C1559790612660%7C1559793268078%7C1041%7C165; _snmp=155979326806631012; _snmb=155979326809359276%7C1559793268115%7C1559793268093%7C1; cityCode=025; provinceCode=100; ");
        headMap.put("Cookie",headMap.get("Cookie") + "authId="+authId + "; secureToken="+ secureToken);

        String responseContent = HttpClientUtil.getInstance().httpGet(url, headMap, getProxy(),true);
        logger.info("harvest请求结果:"+responseContent);
    }

    /**
     * 喂食
     * @param authId
     * @param secureToken
     */
    public void feed(String authId,String secureToken){
        logger.info("feed请求开始:authId:{},secureToken:{}",authId,secureToken);
        String url = "https://sign.suning.com/sign-web/m/ggame/develop/feed.do?foodCode=nut&_=1560217113916";
        Map<String, String> headMap = new HashMap<String,String>();
        headMap.put(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        headMap.put(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        headMap.put(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        headMap.put(HttpHeaders.CACHE_CONTROL, "max-age=0");
        headMap.put(HttpHeaders.CONNECTION, "keep-alive");
        headMap.put(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        headMap.put(HttpHeaders.HOST,"sign.suning.com");
        headMap.put("Upgrade-Insecure-Requests","1");
        headMap.put("Cookie","_snvd=1551928678414LGgh5aE6T+S; _df_ud=13dc381c-9638-4d25-8956-19ea8fbf4384; hm_guid=02fe1c32-8643-4a8e-9dcb-e20ca2cd2dfb; _device_session_id=p_3ea16061-9a65-4d93-bb19-d448f6f47dbf; _cp_dt=81ad985b-9618-4aef-8811-8688eb0b9109-79029; _ga=GA1.2.417107482.1554258543; snadt-id=%7C155425854321884532%7C%7C%7C%7C%7C; Hm_lvt_cb12e33a15345914e449a2ed82a2a216=1556096241; smidV2=20190514162412077b675e1e1e40a0c054f32e870d1229005de06214c8c34c0; route=67d8bd4b0241ec29a6f02b856653c72c; _snadtp=5; _snms=15597335617553748; mtisAbTest=B; mtisCartQty=9; imAuthId=si5CBB8EA4F6FFEEEFB6CF7066ADC9F8FE; cityId=9173; districtId=11375; _snzwt=THKhor16b44182728LwPce496; SN_CITY=100_025_1000173_9173_11_11375_3_0; payMA=211; p_sessionId=KELhtL8SN9agnQTecu58ABR51jh6XFwrBVuckU3C22ib4HTslA; EPPTGC=211*TGT0CDDACE0F7587329CFB6AACC830C5A242205359C; eppAuthId=211%23siF783AF4A7665CE675590016CAE12051E; eppSecureToken=BC054849B0C49C82103583667956F4DB; _gid=GA1.2.141493448.1560235688; tradeMA=133; custLevel=161000000140; custno=6073169285; idsLoginUserIdLastTime=18261941949; logonStatus=0; nick=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; nick2=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; sncnstr=ON50bFT4VCNUT%2FKz%2Fag10Q%3D%3D; sfp_sessionId=2BSPOyoVoP2DaTF3kHEYaL5csEblYuk027QigzGrTWItCLVq1s; _snsr=mms%7Cmms_crm%7C%7CSZ20190609101642591%7C%25E8%258B%258F%25E5%25AE%2581%25E9%2587%2591%25E8%259E%258D*%3A*; smhst=10263045938|0000000000a10877947682|0000000000a102654169|0000000000a102632496|0000000000a102632495|0000000000a102632497|0000000000a177548181|0000000000a11124272076|0000000000a11125615766|0000000000a11048860940|0000000000a11087921955|0070144811a10915605220|0000000000a688589033|0000000000a10624736318|0070236733a10632770490|0070236733a10729485446|0070236733a826232219|0000000000a10991736272|0000000000a10903804877|0070061997a138817752|0070061997; _snma=1%7C155368611691865628%7C1553686116918%7C1560235972916%7C1560235993455%7C1086%7C174; _snmp=156023599048743580; _snmb=156023595421130386%7C1560235993517%7C1560235993480%7C4; _snck=156023601722713268; ");
        headMap.put("Cookie",headMap.get("Cookie") + "authId="+authId + "; secureToken="+ secureToken);

        String responseContent = HttpClientUtil.getInstance().httpGet(url, headMap, getProxy(),true);
        logger.info("feed请求结果:"+responseContent);
    }

    /**
     * 喂食
     * @param authId
     * @param secureToken
     */
    public void feedRest(String authId,String secureToken){
        logger.info("feedRest请求开始:authId:{},secureToken:{}",authId,secureToken);
        String url = "https://sign.suning.com/sign-web/m/ggame/develop/feed.do?foodCode=nut&_=1560217113916";
        org.springframework.http.HttpHeaders requestHeaders = new org.springframework.http.HttpHeaders();

        requestHeaders.add(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        requestHeaders.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        requestHeaders.add(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        requestHeaders.add(HttpHeaders.CACHE_CONTROL, "max-age=0");
        requestHeaders.add(HttpHeaders.CONNECTION, "keep-alive");
        requestHeaders.add(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        requestHeaders.add(HttpHeaders.HOST,"sign.suning.com");
        requestHeaders.add("Upgrade-Insecure-Requests","1");
        requestHeaders.add("Cookie","_snvd=1551928678414LGgh5aE6T+S; _df_ud=13dc381c-9638-4d25-8956-19ea8fbf4384; hm_guid=02fe1c32-8643-4a8e-9dcb-e20ca2cd2dfb; _device_session_id=p_3ea16061-9a65-4d93-bb19-d448f6f47dbf; _cp_dt=81ad985b-9618-4aef-8811-8688eb0b9109-79029; _ga=GA1.2.417107482.1554258543; snadt-id=%7C155425854321884532%7C%7C%7C%7C%7C; Hm_lvt_cb12e33a15345914e449a2ed82a2a216=1556096241; smidV2=20190514162412077b675e1e1e40a0c054f32e870d1229005de06214c8c34c0; route=67d8bd4b0241ec29a6f02b856653c72c; _snadtp=5; _snms=15597335617553748; mtisAbTest=B; mtisCartQty=9; imAuthId=si5CBB8EA4F6FFEEEFB6CF7066ADC9F8FE; cityId=9173; districtId=11375; _snzwt=THKhor16b44182728LwPce496; SN_CITY=100_025_1000173_9173_11_11375_3_0; payMA=211; p_sessionId=KELhtL8SN9agnQTecu58ABR51jh6XFwrBVuckU3C22ib4HTslA; EPPTGC=211*TGT0CDDACE0F7587329CFB6AACC830C5A242205359C; eppAuthId=211%23siF783AF4A7665CE675590016CAE12051E; eppSecureToken=BC054849B0C49C82103583667956F4DB; _gid=GA1.2.141493448.1560235688; tradeMA=133; custLevel=161000000140; custno=6073169285; idsLoginUserIdLastTime=18261941949; logonStatus=0; nick=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; nick2=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; sncnstr=ON50bFT4VCNUT%2FKz%2Fag10Q%3D%3D; sfp_sessionId=2BSPOyoVoP2DaTF3kHEYaL5csEblYuk027QigzGrTWItCLVq1s; _snsr=mms%7Cmms_crm%7C%7CSZ20190609101642591%7C%25E8%258B%258F%25E5%25AE%2581%25E9%2587%2591%25E8%259E%258D*%3A*; smhst=10263045938|0000000000a10877947682|0000000000a102654169|0000000000a102632496|0000000000a102632495|0000000000a102632497|0000000000a177548181|0000000000a11124272076|0000000000a11125615766|0000000000a11048860940|0000000000a11087921955|0070144811a10915605220|0000000000a688589033|0000000000a10624736318|0070236733a10632770490|0070236733a10729485446|0070236733a826232219|0000000000a10991736272|0000000000a10903804877|0070061997a138817752|0070061997; _snma=1%7C155368611691865628%7C1553686116918%7C1560235972916%7C1560235993455%7C1086%7C174; _snmp=156023599048743580; _snmb=156023595421130386%7C1560235993517%7C1560235993480%7C4; _snck=156023601722713268; "+ "authId="+authId + "; secureToken="+ secureToken);
        org.springframework.http.HttpEntity<String> requestEntity = new org.springframework.http.HttpEntity<String>(null, requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        logger.info("feedRest请求结果:"+response.getBody());
    }

    /**
     * 安抚
     * @param authId
     * @param secureToken
     */
    public void appease(String authId,String secureToken){
        logger.info("appease请求开始:authId:{},secureToken:{}",authId,secureToken);
        String url = "https://sign.suning.com/sign-web/m/ggame/develop/appease.do?_=1563430148818";
        Map<String, String> headMap = new HashMap<String,String>();
        headMap.put(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        headMap.put(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        headMap.put(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        headMap.put(HttpHeaders.CACHE_CONTROL, "max-age=0");
        headMap.put(HttpHeaders.CONNECTION, "keep-alive");
        headMap.put(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        headMap.put(HttpHeaders.HOST,"sign.suning.com");
        headMap.put("Upgrade-Insecure-Requests","1");
        headMap.put("Cookie","_snvd=1551928678414LGgh5aE6T+S; _df_ud=13dc381c-9638-4d25-8956-19ea8fbf4384; hm_guid=02fe1c32-8643-4a8e-9dcb-e20ca2cd2dfb; _device_session_id=p_3ea16061-9a65-4d93-bb19-d448f6f47dbf; _cp_dt=81ad985b-9618-4aef-8811-8688eb0b9109-79029; _ga=GA1.2.417107482.1554258543; snadt-id=%7C155425854321884532%7C%7C%7C%7C%7C; Hm_lvt_cb12e33a15345914e449a2ed82a2a216=1556096241; smidV2=20190514162412077b675e1e1e40a0c054f32e870d1229005de06214c8c34c0; route=67d8bd4b0241ec29a6f02b856653c72c; _snadtp=5; _snms=15597335617553748; mtisAbTest=B; mtisCartQty=9; imAuthId=si5CBB8EA4F6FFEEEFB6CF7066ADC9F8FE; cityId=9173; districtId=11375; _snzwt=THKhor16b44182728LwPce496; SN_CITY=100_025_1000173_9173_11_11375_3_0; payMA=211; p_sessionId=KELhtL8SN9agnQTecu58ABR51jh6XFwrBVuckU3C22ib4HTslA; EPPTGC=211*TGT0CDDACE0F7587329CFB6AACC830C5A242205359C; eppAuthId=211%23siF783AF4A7665CE675590016CAE12051E; eppSecureToken=BC054849B0C49C82103583667956F4DB; _gid=GA1.2.141493448.1560235688; tradeMA=133; custLevel=161000000140; custno=6073169285; idsLoginUserIdLastTime=18261941949; logonStatus=0; nick=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; nick2=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; sncnstr=ON50bFT4VCNUT%2FKz%2Fag10Q%3D%3D; sfp_sessionId=2BSPOyoVoP2DaTF3kHEYaL5csEblYuk027QigzGrTWItCLVq1s; _snsr=mms%7Cmms_crm%7C%7CSZ20190609101642591%7C%25E8%258B%258F%25E5%25AE%2581%25E9%2587%2591%25E8%259E%258D*%3A*; smhst=10263045938|0000000000a10877947682|0000000000a102654169|0000000000a102632496|0000000000a102632495|0000000000a102632497|0000000000a177548181|0000000000a11124272076|0000000000a11125615766|0000000000a11048860940|0000000000a11087921955|0070144811a10915605220|0000000000a688589033|0000000000a10624736318|0070236733a10632770490|0070236733a10729485446|0070236733a826232219|0000000000a10991736272|0000000000a10903804877|0070061997a138817752|0070061997; _snma=1%7C155368611691865628%7C1553686116918%7C1560235972916%7C1560235993455%7C1086%7C174; _snmp=156023599048743580; _snmb=156023595421130386%7C1560235993517%7C1560235993480%7C4; _snck=156023601722713268; "+ "authId="+authId + "; secureToken="+ secureToken);

        String responseContent = HttpClientUtil.getInstance().httpGet(url, headMap, getProxy(),true);
        logger.info("appease请求结果:"+responseContent);
    }

    /**
     * 偷云钻
     * @param authId
     * @param secureToken
     */
    public void steal(String authId,String secureToken){
        logger.info("steal请求开始:authId:{},secureToken:{}",authId,secureToken);
        String friendUrl = "https://sign.suning.com/sign-web/m/ggame/social/friendList.do?pageNum=1&_=1564122197640";
        String nearbyUrl = "https://sign.suning.com/sign-web/m/ggame/social/nearbyList.do?pageNum=1&_=1564122197643";
        String friendHomeUrl = "https://sign.suning.com/sign-web/m/ggame/develop/homeInfo.do?friendMemberId=%s&_=1564123214135";
        String url = "https://sign.suning.com/sign-web/m/ggame/develop/steal.do?dropletCode=%s&friendMemberId=%s&_=1564123489101";
        Map<String, String> headMap = new HashMap<String,String>();
        headMap.put(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        headMap.put(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        headMap.put(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        headMap.put(HttpHeaders.CACHE_CONTROL, "max-age=0");
        headMap.put(HttpHeaders.CONNECTION, "keep-alive");
        headMap.put(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        headMap.put(HttpHeaders.HOST,"sign.suning.com");
        headMap.put("Upgrade-Insecure-Requests","1");
        headMap.put("Cookie","_snvd=1551928678414LGgh5aE6T+S; _df_ud=13dc381c-9638-4d25-8956-19ea8fbf4384; hm_guid=02fe1c32-8643-4a8e-9dcb-e20ca2cd2dfb; _device_session_id=p_3ea16061-9a65-4d93-bb19-d448f6f47dbf; _cp_dt=81ad985b-9618-4aef-8811-8688eb0b9109-79029; _ga=GA1.2.417107482.1554258543; snadt-id=%7C155425854321884532%7C%7C%7C%7C%7C; Hm_lvt_cb12e33a15345914e449a2ed82a2a216=1556096241; smidV2=20190514162412077b675e1e1e40a0c054f32e870d1229005de06214c8c34c0; route=67d8bd4b0241ec29a6f02b856653c72c; _snadtp=5; _snms=15597335617553748; mtisAbTest=B; mtisCartQty=9; imAuthId=si5CBB8EA4F6FFEEEFB6CF7066ADC9F8FE; cityId=9173; districtId=11375; _snzwt=THKhor16b44182728LwPce496; SN_CITY=100_025_1000173_9173_11_11375_3_0; payMA=211; p_sessionId=KELhtL8SN9agnQTecu58ABR51jh6XFwrBVuckU3C22ib4HTslA; EPPTGC=211*TGT0CDDACE0F7587329CFB6AACC830C5A242205359C; eppAuthId=211%23siF783AF4A7665CE675590016CAE12051E; eppSecureToken=BC054849B0C49C82103583667956F4DB; _gid=GA1.2.141493448.1560235688; tradeMA=133; custLevel=161000000140; custno=6073169285; idsLoginUserIdLastTime=18261941949; logonStatus=0; nick=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; nick2=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; sncnstr=ON50bFT4VCNUT%2FKz%2Fag10Q%3D%3D; sfp_sessionId=2BSPOyoVoP2DaTF3kHEYaL5csEblYuk027QigzGrTWItCLVq1s; _snsr=mms%7Cmms_crm%7C%7CSZ20190609101642591%7C%25E8%258B%258F%25E5%25AE%2581%25E9%2587%2591%25E8%259E%258D*%3A*; smhst=10263045938|0000000000a10877947682|0000000000a102654169|0000000000a102632496|0000000000a102632495|0000000000a102632497|0000000000a177548181|0000000000a11124272076|0000000000a11125615766|0000000000a11048860940|0000000000a11087921955|0070144811a10915605220|0000000000a688589033|0000000000a10624736318|0070236733a10632770490|0070236733a10729485446|0070236733a826232219|0000000000a10991736272|0000000000a10903804877|0070061997a138817752|0070061997; _snma=1%7C155368611691865628%7C1553686116918%7C1560235972916%7C1560235993455%7C1086%7C174; _snmp=156023599048743580; _snmb=156023595421130386%7C1560235993517%7C1560235993480%7C4; _snck=156023601722713268; "+ "authId="+authId + "; secureToken="+ secureToken);

        String responseContent = HttpClientUtil.getInstance().httpGet(friendUrl, headMap, getProxy(),true);
        JSONArray friends = JSON.parseObject(responseContent).getJSONObject("data").getJSONArray("friends");
        String responseContent1 = HttpClientUtil.getInstance().httpGet(nearbyUrl, headMap, getProxy(),true);
        JSONArray nearbyPeople = JSON.parseObject(responseContent1).getJSONObject("data").getJSONArray("nearbyPeople");
        friends.addAll(nearbyPeople);
        JSONObject friend = null;
        if(friends!= null && !friends.isEmpty()){
            for(Object obj : friends){
                friend = (JSONObject)obj;
                if("1".equals(friend.getString("isSteal"))){
                    responseContent = HttpClientUtil.getInstance().httpGet(String.format(friendHomeUrl,friend.getString("memberId")), headMap, getProxy(),true);
                    JSONArray droplets = JSON.parseObject(responseContent).getJSONObject("data").getJSONArray("dropletDTO");
                    if(droplets != null){
                        JSONObject droplet = null;
                        for(Object o : droplets){
                            droplet = (JSONObject)o;
                            if(!"0".equals(droplet.getString("dropletCode"))){
                                responseContent = HttpClientUtil.getInstance().httpGet(String.format(url,droplet.getString("dropletCode"),friend.getString("memberId")), headMap, getProxy(),true);
                                logger.info("steal请求结果:"+responseContent);
                            }
                        }
                    }
                }
            }
        }
        logger.info("steal请求结束");
    }

    /**
     * 签到
     * @param authId
     * @param secureToken
     */
    public void doSign(String authId,String secureToken){
        logger.info("doSign请求开始:authId:{},secureToken:{}",authId,secureToken);
        String url = "https://sign.suning.com/sign-web/m/ggame/member/doSign.do?dfpToken=THybuI16b258cdc32bUH43bef&channelType=4";
        Map<String, String> headMap = new HashMap<String,String>();
        headMap.put(HttpHeaders.ACCEPT, "*/*");
        headMap.put(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        headMap.put(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        headMap.put(HttpHeaders.CACHE_CONTROL, "max-age=0");
        headMap.put(HttpHeaders.CONNECTION, "keep-alive");
        headMap.put(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        headMap.put(HttpHeaders.HOST,"sign.suning.com");
        headMap.put("Upgrade-Insecure-Requests","1");
        headMap.put("Cookie","_snvd=1551928678414LGgh5aE6T+S; _df_ud=13dc381c-9638-4d25-8956-19ea8fbf4384; hm_guid=02fe1c32-8643-4a8e-9dcb-e20ca2cd2dfb; _device_session_id=p_3ea16061-9a65-4d93-bb19-d448f6f47dbf; _cp_dt=81ad985b-9618-4aef-8811-8688eb0b9109-79029; _ga=GA1.2.417107482.1554258543; snadt-id=%7C155425854321884532%7C%7C%7C%7C%7C; Hm_lvt_cb12e33a15345914e449a2ed82a2a216=1556096241; custno=6073169285; idsLoginUserIdLastTime=18261941949; sncnstr=ON50bFT4VCNUT%2FKz%2Fag10Q%3D%3D; tradeMA=133; smidV2=20190514162412077b675e1e1e40a0c054f32e870d1229005de06214c8c34c0; districtId=11375; SN_CITY=100_025_1000173_9173_11_11375_1_1; payMA=59; logonStatus=0; nick=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; nick2=%E6%B1%9F%E8%8B%8F%E9%A3%8E%E8%A1%8C%E5%A4%A9%E4%B8%8B...; _gid=GA1.2.58559421.1559724357; custLevel=161000000140; route=67d8bd4b0241ec29a6f02b856653c72c; cityId=9173; totalProdQty=5; _snadtp=5; _snms=15597335617553748; sfp_sessionId=RryFAF5P4UjtPmul2fCXCE4Q6DvcEA753JcMkxjsS4EJ1s1IZL; hasTrustToEpp=6073169285; _snzwt=THQt8q16b2aa1a0e04Z5ec230; _snsr=baidu%7Cbrand-jr%7C%7Ctitle%7C%25E8%258B%258F%25E5%25AE%2581%25E9%2587%2591%25E8%259E%258D*%3A*; EPPTGC=59*TGT92F7430DEA9A7DE809744887A473FF625B4317E4; eppAuthId=59%23si99D791DFF35FDA88DDBE7FDB05FF1D48; eppSecureToken=E72F4D8AE806061F585CBF8A4BB3F8A4; smhst=177548181|0000000000a11124272076|0000000000a11125615766|0000000000a11048860940|0000000000a11087921955|0070144811a10915605220|0000000000a688589033|0000000000a10624736318|0070236733a10632770490|0070236733a10729485446|0070236733a826232219|0000000000a10991736272|0000000000a10903804877|0070061997a138817752|0070061997a11110565309|0070061997a10721444278|0070061997a11044430469|0000000000a10263045938|0000000000a10263045922|0000000000a10946938759|0000000000; mtisAbTest=B; mtisCartQty=9; _snma=1%7C155368611691865628%7C1553686116918%7C1559790612660%7C1559793268078%7C1041%7C165; _snmp=155979326806631012; _snmb=155979326809359276%7C1559793268115%7C1559793268093%7C1; cityCode=025; provinceCode=100; ");
        headMap.put("Cookie",headMap.get("Cookie") + "authId="+authId + "; secureToken="+ secureToken);

        String responseContent = HttpClientUtil.getInstance().httpGet(url, headMap, getProxy(),true);
        logger.info("doSign请求结果:"+responseContent);
    }
}
