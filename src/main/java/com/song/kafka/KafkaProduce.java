package com.song.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * Created by feng on 2019/9/21.
 * LEADER_NOT_AVAILABLE问题 客户端登录zookeeper，输入命令  rmr /brokers
 */
public class KafkaProduce {

    private static Properties properties;

    static {
        properties = new Properties();
        properties.put("bootstrap.servers","127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094");
        properties.put("producer.type","sync");
        properties.put("request.required.acks","1");
        properties.put("serializer.class","kafka.serializer.DefaultEncoder");
        properties.put("key.serializer","org.apache.kafka.common.serialization.ByteArraySerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.ByteArraySerializer");
        properties.put("bak.partitioner.class","kafka.producer.DefaultPartitioner");
        properties.put("bak.key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("bak.value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("num.partitions", "3");
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param key
     * @param value
     */
    public void sendMsg(String topic, byte[] key, byte[] value) {
        // 实例化produce
        KafkaProducer<byte[], byte[]> kp = new KafkaProducer<byte[], byte[]>(
                properties);

        // 消息封装 通过key保证发送到同一个分区，实现消息发送端消息顺序，还可以指定分区
        //ProducerRecord<byte[], byte[]> pr = new ProducerRecord<byte[], byte[]>(topic,Integer partition, key, value);
        //不指定分区，也不要求顺序性
        //ProducerRecord<byte[], byte[]> pr = new ProducerRecord<byte[], byte[]>(topic, value);
        ProducerRecord<byte[], byte[]> pr = new ProducerRecord<byte[], byte[]>(
                topic, key, value);
        // 发送数据
        kp.send(pr, new Callback() {
            // 回调函数
            @Override
            public void onCompletion(RecordMetadata metadata,
                                     Exception exception) {
                if (null != exception) {
                    System.out.println("记录的offset在:" + metadata.offset());
                    System.out.println(exception.getMessage() + exception);
                }
            }
        });

        // 关闭produce
        kp.close();
    }

    public static void main(String[] args) {

        for(int i = 1; i <= 100; i++){
            //String topic, String key, String value
            final int j = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    KafkaProduce kafkaProduce = new KafkaProduce();
                    //按照规则发送到三个分区中
                    //kafkaProduce.sendMsg("blog",("partition[" + j + "]").getBytes(),("message[The " + j + " message]").getBytes());

                    //key相同发送到同一个分区
                    kafkaProduce.sendMsg("blog","test".getBytes(),("message[The " + j + " message]").getBytes());
                }
            }).start();
        }
    }
}