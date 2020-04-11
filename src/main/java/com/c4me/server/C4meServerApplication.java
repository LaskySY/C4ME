package com.c4me.server;

import com.c4me.server.config.constant.Const;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.c4me.server.core.*.repository"})
public class C4meServerApplication {

    @PostConstruct
    public static void init() {
        Const.Filenames.readConfigFile();
    }

    public static void main(String[] args) {
        SpringApplication.run(C4meServerApplication.class, args);
    }

}
