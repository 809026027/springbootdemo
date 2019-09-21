package com.song.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by 17060342 on 2019/6/6.
 */
@Component
public class EmailBean {
    @Value("${mail.smtp.auth}")
    private String auth;
    @Value("${mail.host}")
    private String host;
    @Value("${mail.transport.protocol}")
    private String protocol;
    @Value("${mail.smtp.port}")
    private int port;
    @Value("${mail.auth.name}")
    private String authName;
    @Value("${mail.auth.password}")
    private String password;
    @Value("${mail.is.ssl}")
    private boolean isSSL;
    @Value("${mail.send.charset}")
    private String charset ;
    @Value("${mail.smtp.timeout}")
    private String timeout;

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSSL() {
        return isSSL;
    }

    public void setSSL(boolean SSL) {
        isSSL = SSL;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}
