package com.register.client.api;

import java.util.Set;

/**
 * Created by kaiwang on 2017/1/6.
 */
public interface IRegisterClient {
    void register();

    void shutdown();

    Set<String> getService(String serviceName);
}
