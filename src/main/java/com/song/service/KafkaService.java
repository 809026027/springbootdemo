package com.song.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by feng on 2019/9/21.
 */
@Service
public class KafkaService {

    /**
     * log
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public String send(String msg){
        kafkaTemplate.send("test_topic", msg);
        return "success";
    }

    @KafkaListener(topics = "test_topic")
    public void listen (ConsumerRecord<?, ?> record) throws Exception {
        logger.info("topic = {}, offset = {}, value = {}", record.topic(), record.offset(), record.value());
    }
}
