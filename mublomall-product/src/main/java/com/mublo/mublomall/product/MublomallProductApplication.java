package com.mublo.mublomall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.mublo.mublomall.product.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class MublomallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MublomallProductApplication.class, args);
    }

}
