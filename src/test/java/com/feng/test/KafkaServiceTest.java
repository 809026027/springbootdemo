package com.feng.test;

import com.song.configuration.Entry;
import com.song.service.KafkaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by feng on 2019/9/21.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Entry.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class KafkaServiceTest {

    @Autowired
    private KafkaService kafkaService;

    @Test
    public void test(){
        kafkaService.send("111");
    }
}
