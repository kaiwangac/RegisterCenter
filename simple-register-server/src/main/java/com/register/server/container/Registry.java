package com.register.server.container;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by kaiwang on 2017/1/4.
 */
public class Registry {
    private static final long SIZE = 1000L;
    private static final long DURATION = 5;

    private Cache<String, String> registryCache;

    public Registry() {
        this.registryCache = CacheBuilder.newBuilder()
                .maximumSize(SIZE)
                .expireAfterWrite(DURATION, TimeUnit.MINUTES)
                .build();

    }

    public Map<String, String> getMap() {
        return this.registryCache.asMap();
    }

    public String get(String key) {
        return this.registryCache.getIfPresent(key);
    }

    public void put(String key, String value) {
        this.registryCache.put(key, value);
    }

    public void delete(String key) {
        this.registryCache.invalidate(key);
    }
}
