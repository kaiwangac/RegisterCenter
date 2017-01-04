package com.register.server;

import com.register.server.config.RegistryConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Created by kaiwang on 2017/1/4.
 */
@SpringBootApplication
@Import(RegistryConfiguration.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.run(args);
    }
}
