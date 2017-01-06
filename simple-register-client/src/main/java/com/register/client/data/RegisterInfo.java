package com.register.client.data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by kaiwang on 2017/1/5.
 */
@ConfigurationProperties(prefix = "register.service")
public class RegisterInfo {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
