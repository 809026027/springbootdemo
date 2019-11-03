package com.song.configuration;

import com.song.rabbitmq.QueueConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by feng on 2019/11/2.
 */
@Configuration
public class MyRabbitMqConfiguration {
    /**
     * 交换配置
     *
     * @return
     */
    @Bean
    public DirectExchange messageDirectExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(QueueConstants.MESSAGE_EXCHANGE)
                .durable()
                .build();
    }

    /**
     * 消息队列声明
     *
     * @return
     */
    @Bean
    public Queue messageQueue() {
        return QueueBuilder.durable(QueueConstants.MESSAGE_QUEUE_NAME)
                .build();
    }

    /**
     * 消息绑定
     *
     * @return
     */
    @Bean
    public Binding messageBinding() {
        return BindingBuilder.bind(messageQueue())
                .to(messageDirectExchange())
                .with(QueueConstants.MESSAGE_ROUTE_KEY);
    }



}
