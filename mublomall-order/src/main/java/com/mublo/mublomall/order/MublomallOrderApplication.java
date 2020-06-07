package com.mublo.mublomall.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MublomallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MublomallOrderApplication.class, args);
    }

}
