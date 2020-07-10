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

public class HttpApiClient短信发送 extends ApacheHttpClient{
    public final static String HOST = "edisim.market.alicloudapi.com";
    static HttpApiClient短信发送 instance = new HttpApiClient短信发送();
    public static HttpApiClient短信发送 getInstance(){
        return instance;}

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTP);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }




    public void 发送短信(String mobile , String templateID , String templateParamSet , String callbackUrl , int channel , ApiCallback callback) {
        String path = "/comms/sms/sendmsg";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("mobile" , mobile , ParamPosition.BODY , true);
        request.addParam("templateID" , templateID , ParamPosition.BODY , true);
        request.addParam("templateParamSet" , templateParamSet , ParamPosition.BODY , false);
        request.addParam("callbackUrl" , callbackUrl , ParamPosition.BODY , false);
        request.addParam("channel" , String.valueOf(channel), ParamPosition.BODY , false);



        sendAsyncRequest(request , callback);
    }

    public ApiResponse 发送短信SyncMode(String mobile , String templateID , String templateParamSet , String callbackUrl , int channel) {
        String path = "/comms/sms/sendmsg";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("mobile" , mobile , ParamPosition.BODY , true);
        request.addParam("templateID" , templateID , ParamPosition.BODY , true);
        request.addParam("templateParamSet" , templateParamSet , ParamPosition.BODY , false);
        request.addParam("callbackUrl" , callbackUrl , ParamPosition.BODY , false);
        request.addParam("channel" , String.valueOf(channel), ParamPosition.BODY , false);



        return sendSyncRequest(request);
    }

}