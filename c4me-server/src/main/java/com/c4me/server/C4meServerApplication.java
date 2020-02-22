package com.c4me.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.c4me.server.core.*.repository"})
public class C4meServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(C4meServerApplication.class, args);
    }

}
