package com.c4me.credential;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class C4MeCredentialApplication {

    public static void main(String[] args) {
        SpringApplication.run(C4MeCredentialApplication.class, args);
    }

}
