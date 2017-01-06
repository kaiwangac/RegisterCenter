package com.register.client.registrar;

import com.alibaba.fastjson.JSONObject;
import com.register.client.api.IRegisterClient;
import com.register.client.data.RegisterInfo;
import com.register.client.data.ServiceInfo;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.register.client.constant.GlobalConstant.AT;
import static com.register.client.constant.GlobalConstant.COLON;

/**
 * Created by kaiwang on 2017/1/4.
 */
public class RegisterClient implements IRegisterClient {
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final int POOL_SIZE = 10;
    private String url;
    private RestTemplate restTemplate;
    private ServiceInfo serviceInfo;
    private ScheduledFuture scheduledFuture;

    public RegisterClient(RegisterInfo registerInfo) {
        this(new RestTemplate(), null, registerInfo);
    }

    public RegisterClient(ServiceInfo serviceInfo, RegisterInfo registerInfo) {
        this(new RestTemplate(), serviceInfo, registerInfo);
    }

    public RegisterClient(RestTemplate restTemplate, ServiceInfo serviceInfo, RegisterInfo registerInfo) {
        this.restTemplate = restTemplate;
        this.serviceInfo = serviceInfo;
        this.url = registerInfo.getUrl();
    }

    @Override
    public void register() {
        Assert.notNull(serviceInfo, "service info must not be null");
        this.registerService(serviceInfo);
        this.sendHeartbeat(serviceInfo);
    }

    @Override
    public void shutdown() {
        Assert.notNull(serviceInfo, "service info must not be null");
        this.scheduledFuture.cancel(true);
        this.shutdown(serviceInfo);
    }

    @Override
    public Map<String, String> getService() {
        return this.restTemplate.getForObject(this.url + "/registry", Map.class);
    }


    private void registerService(ServiceInfo serviceInfo) {
        this.restTemplate.postForEntity(this.url + "/registry", getEntity(serviceInfo), String.class);
    }

    private void shutdown(ServiceInfo serviceInfo) {
        this.restTemplate.delete(this.url + "/registry/{key}", getKey(serviceInfo));
    }

    private void sendHeartbeat(ServiceInfo serviceInfo) {
        Heartbeat heartbeat = new Heartbeat(serviceInfo, this.restTemplate, this.url);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(POOL_SIZE);
        this.scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(heartbeat, 200, 250, TimeUnit.SECONDS);
    }

    private Map<String, String> getEntity(ServiceInfo serviceInfo) {
        Map<String, String> entity = new HashMap<>();
        entity.put(KEY, getKey(serviceInfo));
        entity.put(VALUE, getValue(serviceInfo));
        return entity;
    }

    private String getKey(ServiceInfo serviceInfo) {
        return serviceInfo.getIpAddress() + COLON + serviceInfo.getPort() + AT + serviceInfo.getServiceName();
    }

    private String getValue(ServiceInfo serviceInfo) {
        return JSONObject.toJSONString(serviceInfo);
    }

    private class Heartbeat implements Runnable {
        private String url;
        private ServiceInfo serviceInfo;
        private RestTemplate restTemplate;

        public Heartbeat(ServiceInfo serviceInfo, RestTemplate restTemplate, String url) {
            this.serviceInfo = serviceInfo;
            this.restTemplate = restTemplate;
            this.url = url;
        }

        @Override
        public void run() {
            this.restTemplate.put(this.url + "/registry", getEntity(serviceInfo));
        }
    }
}
