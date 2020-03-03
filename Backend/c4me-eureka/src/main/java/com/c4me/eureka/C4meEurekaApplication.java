package com.c4me.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class C4meEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(C4meEurekaApplication.class, args);
    }

}
