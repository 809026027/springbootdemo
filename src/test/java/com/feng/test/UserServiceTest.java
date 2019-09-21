package com.feng.test;

import com.song.configuration.Entry;
import com.song.entity.EmailBean;
import com.song.mapper.UserMapper;
import com.song.repositoty.UserRepositoty;
import com.song.service.UserService;
import com.song.task.CrawlTask;
import com.song.task.SNTask;
import com.song.utils.FeiGeUtil;
import com.song.utils.JDUtil;
import com.song.utils.TaobaoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Junit测试类
 * Created by 17060342 on 2019/6/4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Entry.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class UserServiceTest {

    /**
     * log日志
     */
    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRepositoty userRepositoty;

    @Autowired
    private UserService userService;

    @Autowired(required = false)
    private SNTask SNTask;

    @Resource
    private TaobaoUtil taobaoUtil;

    @Resource
    private CrawlTask crawlTask;

    @Resource
    private FeiGeUtil feiGeUtil;

    @Autowired
    private EmailBean emailBean;

    @Test
    public void testFindAllUsers() {
        System.out.println(userMapper.findAllUsers());
    }

    @Test
    public void testFindAllUsers1() {
        System.out.println(userRepositoty.findByUserName("song"));
    }

    @Test
    public void testTask() {
        //scheduleTask.signSldesTask();
        //scheduleTask.doSignTask();
        //scheduleTask.harvestTask();
        //scheduleTask.feedTask();
        //scheduleTask.appeaseTask();
        //scheduleTask.collectDropletTask();
        //scheduleTask.yunzuanTask();
        //scheduleTask.dripTask();
        //SNTask.stealTask();
        //taobaoUtil.gaoyongzhuanlian(TaobaoUtil.APP_PID,"522648528552","2");
        //taobaoUtil.gaoyongzhuanlian_tkl(TaobaoUtil.APP_PID,"￥ocPBYiOnPlw￥","2");
        //taobaoUtil.tkl_create(TaobaoUtil.APP_PID,"美美小编精心推荐",taobaoUtil.gaoyongzhuanlian(TaobaoUtil.APP_PID,"522648528552","2").getString("coupon_click_url"),"https://img.alicdn.com/bao/uploaded/i2.jpg_200x200.jpg","2");
        //taobaoUtil.getShorturl(TaobaoUtil.APP_PID, URLUtil.getURLEncoderString("https://uland.taobao.com/coupon/edetail?activityId=b58306919b894859ad24d53e0360a55b&itemId=574170687895&pid=mm_46736561_19694158_68262194"));
        //JDUtil.getPromotionUrl("23484023378");
    }

    @Test
    public void testTransactionA() {
        try{
            userService.transactionA();
        }catch(Exception e){
            logger.error("testTransactionA",e);
        }
    }

    @Test
    public void testTransactionB() {
        try{
            userService.transactionB();
        }catch(Exception e){
            logger.error("testTransactionB",e);
        }
    }

    @Test
    public void testTransactionC() {
        try{
            userService.transactionC();
        }catch(Exception e){
            logger.error("testTransactionC",e);
        }
    }

    @Test
    public void testTransactionD() {
        try{
            userService.transactionD();
        }catch(Exception e){
            logger.error("testTransactionD",e);
        }
    }

    @Test
    public void testMybatisCacheOne() {
        try{
            userService.mybatisCacheOne();
        }catch(Exception e){
            logger.error("testMybatisCacheOne",e);
        }
    }

    @Test
    public void testMySQLRR() {
        try{
            userService.mySQLRR1();
            userService.mySQLRR2();
        }catch(Exception e){
            logger.error("testMybatisCacheOne",e);
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e1) {
        }
    }

    @Test
    public void testMySQLRR1() {
        try{
            userService.mySQLRR3();
            userService.mySQLRR4();
        }catch(Exception e){
            logger.error("testMybatisCacheOne",e);
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e1) {
        }
    }

    @Test
    public void testCrawlGoods() {
        try{
            //String response = crawlTask.crawlGoods("荣耀V20");
            //List<Map<String,Object>> list = crawlTask.crawlGoodInfo(response);
            //crawlTask.crawlComment(list);
            //crawlTask.tuan0818Task();
            crawlTask.tuan0818Task();
            //crawlTask.tuan818Detail("http://www.0818tuan.com/xbhd/233458.html");
            //feiGeUtil.sendCuXiaoMsg("");
        }catch(Exception e){
            logger.error("testCrawlGoods",e);
        }
    }
}
