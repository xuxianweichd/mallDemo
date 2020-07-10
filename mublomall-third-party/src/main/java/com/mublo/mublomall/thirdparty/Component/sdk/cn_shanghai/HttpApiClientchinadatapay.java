//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.mublo.mublomall.thirdparty.Component.sdk.cn_shanghai;

import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;


public class HttpApiClientchinadatapay extends ApacheHttpClient{
    public final static String HOST = "checkone.market.alicloudapi.com";
    static HttpApiClientchinadatapay instance = new HttpApiClientchinadatapay();
    public static HttpApiClientchinadatapay getInstance(){return instance;}

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTP);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }




    public void 实名认证(String name , String idcard , ApiCallback callback) {
        String path = "/chinadatapay/1882";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("name" , name , ParamPosition.BODY , true);
        request.addParam("idcard" , idcard , ParamPosition.BODY , true);



        sendAsyncRequest(request , callback);
    }

    public ApiResponse 实名认证SyncMode(String name , String idcard) {
        String path = "/chinadatapay/1882";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("name" , name , ParamPosition.BODY , true);
        request.addParam("idcard" , idcard , ParamPosition.BODY , true);



        return sendSyncRequest(request);
    }

}