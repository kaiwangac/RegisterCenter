package com.register.client.status;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.register.client.data.ServiceInfo;
import com.register.client.registrar.RegisterClient;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.register.client.constant.GlobalConstant.AT;
import static com.register.client.constant.GlobalConstant.COLON;

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
                .expireAfterAccess(DURATION, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Set<ServiceInfo>>() {
                    @Override
                    public Set<ServiceInfo> load(String service) throws Exception {
                        return getServiceInfo(service);
                    }
                });
    }

    private Set<ServiceInfo> getServiceInfo(String service) {
        Set<String> set = this.registerClient.getService(service);
        Set<ServiceInfo> serviceInfoSet = new HashSet<ServiceInfo>();
        for (String s : set) {
            ServiceInfo serviceInfo = new ServiceInfo();
            serviceInfo.setServiceName(s.split(AT)[1]);
            serviceInfo.setIpAddress(s.split(COLON)[0]);
            serviceInfo.setPort(Integer.parseInt(s.split(AT)[0].split(COLON)[1]));
            serviceInfoSet.add(serviceInfo);
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
