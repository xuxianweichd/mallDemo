package com.mublo.mublomall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.mublo.mublomall.product.dao")
@EnableFeignClients("com.mublo.mublomall.product.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class MublomallProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(MublomallProductApplication.class, args);
    }
}
