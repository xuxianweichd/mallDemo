/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1.导入依赖
 * 2.编写配置,给容器中注入一个RestHighLevelClient
 */
@Configuration
public class MublomallElasticSearchConfig {

    /**
     *配置请求要加入的参数
     */
    public static final RequestOptions COMMON_OPTIONS ;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        //发送请求之前构建请求头信息
        //builder.addHeader("Authorization", "Bearer " + TOKEN);
        //响应的消费者，异步请求
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory
//                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient(){
        RestHighLevelClient client=new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.56.10",9200,"http")
                )
        );
        System.out.println("=============192.168.56.10==============");
//        RestHighLevelClient client = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost("192.168.56.10",9200,"http")
//                )
//        );
        return client;
    }
}
