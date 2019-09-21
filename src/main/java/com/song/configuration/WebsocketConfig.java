package com.song.configuration;

import com.song.listener.RequestListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Created by 17060342 on 2019/6/12.
 */
@Configuration
public class WebsocketConfig {

    @Autowired
    private RequestListener requestListener;
    /**
     * <br>描 述:    @Endpoint注解的websocket交给ServerEndpointExporter自动注册管理
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

    @Bean
    public ServletListenerRegistrationBean<RequestListener> getServletListenerRegistrationBean(){
        ServletListenerRegistrationBean<RequestListener> bean = new ServletListenerRegistrationBean<RequestListener>();
        bean.setListener(requestListener);
        return bean;
    }
}
