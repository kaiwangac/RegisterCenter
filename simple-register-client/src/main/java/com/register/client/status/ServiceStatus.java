package com.register.client.status;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.register.client.data.ServiceInfo;
import com.register.client.registrar.RegisterClient;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by kaiwang on 2017/1/6.
 */
public class ServiceStatus {
    private static final long DURATION = 120;

    private RegisterClient registerClient;
    private LoadingCache<String, Set<ServiceInfo>> statusCache;

    public ServiceStatus(RegisterClient registerClient) {
        this.registerClient = registerClient;
        this.statusCache = CacheBuilder.newBuilder()
                .refreshAfterWrite(DURATION, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Set<ServiceInfo>>() {
                    @Override
                    public Set<ServiceInfo> load(String service) throws Exception {
                        return getServiceInfo(service);
                    }
                });
    }

    private Set<ServiceInfo> getServiceInfo(String service) {
        Map<String, String> map = this.registerClient.getService();
        Set<ServiceInfo> serviceInfoSet = new HashSet<ServiceInfo>();
        for (String key : map.keySet()) {
            if (key.contains(service)) {
                serviceInfoSet.add(JSONObject.parseObject(map.get(key), ServiceInfo.class));
            }
        }
        return serviceInfoSet;
    }

    public Set<ServiceInfo> get(String service) {
        try {
            return this.statusCache.get(service);
        } catch (ExecutionException e) {
            return null;
        }
    }
}
