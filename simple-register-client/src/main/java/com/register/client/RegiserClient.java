package com.register.client;

import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by kaiwang on 2017/1/4.
 */
public class RegiserClient {
    private static final String URL = "http://localhost:8972/";
    private static final String COLON = ":";
    private static final String AT = "@";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final int POOL_SIZE = 10;
    private RestTemplate restTemplate;
    private ServiceInfo serviceInfo;
    private ScheduledFuture scheduledFuture;

    public RegiserClient() {
        this.restTemplate = new RestTemplate();
    }

    public RegiserClient(ServiceInfo serviceInfo) {
        this.restTemplate = new RestTemplate();
        this.serviceInfo = serviceInfo;
    }

    public RegiserClient(RestTemplate restTemplate, ServiceInfo serviceInfo) {
        this.restTemplate = restTemplate;
        this.serviceInfo = serviceInfo;
    }

    public void register() {
        Assert.notNull(serviceInfo, "service info must not be null");
        this.registerService(serviceInfo);
        this.sendHeartbeat(serviceInfo);
    }

    public void shutdown() {
        Assert.notNull(serviceInfo, "service info must not be null");
        this.scheduledFuture.cancel(true);
        this.shutdown(serviceInfo);
    }


    private void registerService(ServiceInfo serviceInfo) {
        this.restTemplate.postForEntity(URL + "/registry", getEntity(serviceInfo), String.class);
    }

    private void shutdown(ServiceInfo serviceInfo) {
        this.restTemplate.delete(URL + "/registry/" + serviceInfo.getIpAddress() + COLON + serviceInfo.getPort() + AT + serviceInfo.getServiceName());
    }

    private void sendHeartbeat(ServiceInfo serviceInfo) {
        Heartbeat heartbeat = new Heartbeat(serviceInfo, this.restTemplate);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(POOL_SIZE);
        this.scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(heartbeat, 200, 250, TimeUnit.SECONDS);
    }

    private Map<String, String> getEntity(ServiceInfo serviceInfo) {
        Map<String, String> entity = new HashMap<>();
        entity.put(KEY, serviceInfo.getIpAddress() + COLON + serviceInfo.getPort() + AT + serviceInfo.getServiceName());
        entity.put(VALUE, serviceInfo.getServiceName());
        return entity;
    }

    private class Heartbeat implements Runnable {
        private ServiceInfo serviceInfo;
        private RestTemplate restTemplate;

        public Heartbeat(ServiceInfo serviceInfo, RestTemplate restTemplate) {
            this.serviceInfo = serviceInfo;
            this.restTemplate = restTemplate;
        }

        @Override
        public void run() {
            this.restTemplate.put(URL + "/registry", getEntity(serviceInfo));
        }
    }
}
