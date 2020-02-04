package com.c4me.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin2.server.internal.EnableZipkinServer;


//@EnableDiscoveryClient
@EnableZipkinServer
@SpringBootApplication
public class C4meZipkinApplication {

    public static void main(String[] args) {
        SpringApplication.run(C4meZipkinApplication.class, args);
    }

}
