package com.feng.test;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.song.configuration.Entry;
import com.song.mapper.PromotionMapper;
import com.song.service.SparkService;
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
public class SparkServiceTest {

    /**
     * log日志
     */
    private static final Logger logger = LoggerFactory.getLogger(SparkServiceTest.class);

    @Resource
    private SparkService sparkService;

    @Test
    public void testSparkSessionMySql() {
        sparkService.sparkMySql();
    }
}
