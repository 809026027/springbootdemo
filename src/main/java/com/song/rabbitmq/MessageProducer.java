package com.song.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by feng on 2019/11/2.
 */
@Component
public class MessageProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String str) {

        rabbitTemplate.convertAndSend(QueueConstants.MESSAGE_EXCHANGE, QueueConstants.MESSAGE_ROUTE_KEY, str);
    }
}
