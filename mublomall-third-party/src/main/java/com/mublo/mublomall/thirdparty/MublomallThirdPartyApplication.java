package com.mublo.mublomall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MublomallThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MublomallThirdPartyApplication.class, args);
    }

}
