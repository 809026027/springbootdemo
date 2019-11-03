package com.song.controller;

import com.song.rabbitmq.MessageProducer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by feng on 2019/11/2.
 */
@RestController
@RequestMapping(value = "/rabbitmq")
public class RabbitMqController {

    /**
     * log
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RabbitMqController.class);

    @Autowired
    private MessageProducer messageProducer;

    @RequestMapping(value = "/index")
    public String index(String str) {
        logger.info("rabbitmq test send:{}",str);
        // 将实体实例写入消息队列
        messageProducer.sendMessage(str);
        return "Success";
    }
}
