package com.song.configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 17060342 on 2019/6/21.
 */
@Configuration
public class ElasticSearchConfig {

    /**
     *
     * @Title: transportClient
     * @Description: 配置elasticsearch
     * @return
     * @throws UnknownHostException
     */
    @Bean
    public TransportClient transportClient() throws UnknownHostException {
        // 一定要注意,9300为elasticsearch的tcp端口
        TransportAddress master = new TransportAddress(InetAddress.getByName("localhost"), 9300);
        //InetSocketTransportAddress node1 = new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9301);
        //InetSocketTransportAddress node2 = new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9302);
        // 集群名称
        Settings settings = Settings.builder()
                //.put("cluster.name", "feng")
                .build();
        //restClient ，TransportClient 两种方式，前面使用 9200端口，后面使用  9300端口
        TransportClient client = new PreBuiltTransportClient(settings);
        // 添加
        client.addTransportAddresses(master);
        return client;
    }

}
