package com.mublo.mublomall.lgateway.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//@Configuration
public class MublomallCorsConfiguration {
//    @Bean
//    public CorsWebFilter corsWebFilter(){
//        UrlBasedCorsConfigurationSource source =new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfiguration=new CorsConfiguration();
////        配置跨域
////        允许的请求头
//        corsConfiguration.addAllowedHeader("*");
////        允许的请求方式
//        corsConfiguration.addAllowedMethod("*");
////        允许的请求来源
////        corsConfiguration.setAllowedHeaders();
//        corsConfiguration.addAllowedOrigin("*");
////        允许携带cookie
//        corsConfiguration.setAllowCredentials(true);
////        注册跨域的配置
//        source.registerCorsConfiguration("/**",corsConfiguration);
//        return new CorsWebFilter(source);
//    }

}
