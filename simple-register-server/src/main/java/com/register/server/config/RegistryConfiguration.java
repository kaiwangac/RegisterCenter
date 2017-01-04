package com.register.server.config;

import com.register.server.container.ContainerCustomizer;
import com.register.server.container.Registry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kaiwang on 2017/1/4.
 */
@Configuration
public class RegistryConfiguration {
    @Bean
    public Registry registry() {
        return new Registry();
    }

    @Bean
    public ContainerCustomizer containerCustomizer() {
        return new ContainerCustomizer();
    }
}
