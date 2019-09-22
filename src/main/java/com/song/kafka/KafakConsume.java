package com.song.kafka;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.consumer.ConsumerConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by feng on 2019/9/21.
 */
public class KafakConsume implements Runnable {

    private ConsumerConfig consumerConfig;
    private static String topic="blog";
    Properties props;
    final int topic_Count_Map = 1;
    final int a_numThreads = 1;

    public KafakConsume() {
        props = new Properties();
        //props.put("zookeeper.connect", "xxx:2181,yyy:2181,zzz:2181");
       props.put("zookeeper.connect", "localhost:2181");
//        props.put("zookeeper.connectiontimeout.ms", "30000");
        props.put("group.id", "blog");
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");
        consumerConfig = new ConsumerConfig(props);
    }

    @Override
    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(topic_Count_Map));
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        ExecutorService executor = Executors.newFixedThreadPool(a_numThreads);
        for (final KafkaStream stream : streams) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    ConsumerIterator<byte[], byte[]> it = stream.iterator();
                    while (it.hasNext()) {
                        MessageAndMetadata<byte[], byte[]> mam = it.next();
                        System.out.println(Thread.currentThread().getName() + ": partition[" + mam.partition() + "],"
                                + "offset[" + mam.offset() + "], " + new String(mam.message()));

                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        System.out.println(topic);
        Thread t = new Thread(new KafakConsume());
        t.start();
    }
}
