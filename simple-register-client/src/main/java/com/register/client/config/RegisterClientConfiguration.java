package com.register.client.config;

import com.register.client.data.RegisterInfo;
import com.register.client.registrar.RegisterClient;
import com.register.client.status.ServiceStatus;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kaiwang on 2017/1/6.
 */
@Configuration
@EnableConfigurationProperties(RegisterInfo.class)
public class RegisterClientConfiguration {
    @Bean
    public RegisterClient regiserClient(RegisterInfo registerInfo) {
        return new RegisterClient(registerInfo);
    }

    @Bean
    public ServiceStatus serviceStatus(RegisterClient registerClient) {
        return new ServiceStatus(registerClient);
    }
}
