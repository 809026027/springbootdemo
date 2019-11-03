package com.song.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by feng on 2019/11/2.
 */
@Component
public class MessageConsumer {
    @RabbitListener(queues = QueueConstants.MESSAGE_QUEUE_NAME)
    public void processMessage(Channel channel, Message message) {
        System.out.println("MessageConsumer收到消息："+new String(message.getBody()));
        try {
            //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            //消息的标识，false只确认当前一个消息收到，true确认所有consumer获得的消息
            // channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            //ack返回false，并重新回到队列，api里面解释得很清楚
            // channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            //拒绝消息
            // channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        } catch (Exception e) {

        }
    }
}
