package com.c4me.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class C4meServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(C4meServerApplication.class, args);
    }

}
