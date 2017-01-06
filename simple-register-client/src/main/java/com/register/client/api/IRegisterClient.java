package com.register.client.api;

import java.util.Map;

/**
 * Created by kaiwang on 2017/1/6.
 */
public interface IRegisterClient {
    void register();

    void shutdown();

    Map<String, String> getService();
}
