package com.feng.test;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.song.configuration.Entry;
import com.song.entity.Promotion;
import com.song.mapper.PromotionMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Junit测试类
 * Created by 17060342 on 2019/6/4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Entry.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class PromotionServiceTest {

    /**
     * log日志
     */
    private static final Logger logger = LoggerFactory.getLogger(PromotionServiceTest.class);

    @Resource
    private PromotionMapper promotionMapper;

    @Test
    public void testFindAllPromotions() {
        PageHelper.startPage(1, 10);
        System.out.println(JSON.toJSONString(promotionMapper.findPagePromotions()));
    }

    @Test
    public void testAddPromotion() {
        Promotion promotion = new Promotion("1", "2");
        promotionMapper.addPromotion(promotion);
        System.out.println(promotion.getId());
    }


}
