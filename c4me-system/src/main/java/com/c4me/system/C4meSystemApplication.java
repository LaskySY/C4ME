package com.c4me.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class C4meSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(C4meSystemApplication.class, args);
    }

}
