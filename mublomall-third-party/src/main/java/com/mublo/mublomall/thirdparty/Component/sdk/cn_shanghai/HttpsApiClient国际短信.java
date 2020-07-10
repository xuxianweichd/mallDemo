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

public class HttpsApiClient国际短信 extends ApacheHttpClient{
    public final static String HOST = "intlsms.market.alicloudapi.com";
    static HttpsApiClient国际短信 instance = new HttpsApiClient国际短信();
    public static HttpsApiClient国际短信 getInstance(){return instance;}

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTPS);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }



    public void 发送国际短信_按量(String mobile , String templateID , String templateParamSet , String callbackUrl , int channel , ApiCallback callback) {
        String path = "/comms/sms/sendmsgall";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("mobile" , mobile , ParamPosition.BODY , true);
        request.addParam("templateID" , templateID , ParamPosition.BODY , true);
        request.addParam("templateParamSet" , templateParamSet , ParamPosition.BODY , false);
        request.addParam("callbackUrl" , callbackUrl , ParamPosition.BODY , false);
        request.addParam("channel" , String.valueOf(channel), ParamPosition.BODY , false);



        sendAsyncRequest(request , callback);
    }

    public ApiResponse 发送国际短信_按量SyncMode(String mobile , String templateID , String templateParamSet , String callbackUrl , int channel) {
        String path = "/comms/sms/sendmsgall";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("mobile" , mobile , ParamPosition.BODY , true);
        request.addParam("templateID" , templateID , ParamPosition.BODY , true);
        request.addParam("templateParamSet" , templateParamSet , ParamPosition.BODY , false);
        request.addParam("callbackUrl" , callbackUrl , ParamPosition.BODY , false);
        request.addParam("channel" , String.valueOf(channel), ParamPosition.BODY , false);



        return sendSyncRequest(request);
    }
    public void 短信群发(String mobileSet , String templateID , String templateParamSet , String callbackUrl , int channel , ApiCallback callback) {
        String path = "/comms/sms/groupmessaging";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("mobileSet" , mobileSet , ParamPosition.BODY , true);
        request.addParam("templateID" , templateID , ParamPosition.BODY , true);
        request.addParam("templateParamSet" , templateParamSet , ParamPosition.BODY , false);
        request.addParam("callbackUrl" , callbackUrl , ParamPosition.BODY , false);
        request.addParam("channel" , String.valueOf(channel), ParamPosition.BODY , false);



        sendAsyncRequest(request , callback);
    }

    public ApiResponse 短信群发SyncMode(String mobileSet , String templateID , String templateParamSet , String callbackUrl , int channel) {
        String path = "/comms/sms/groupmessaging";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("mobileSet" , mobileSet , ParamPosition.BODY , true);
        request.addParam("templateID" , templateID , ParamPosition.BODY , true);
        request.addParam("templateParamSet" , templateParamSet , ParamPosition.BODY , false);
        request.addParam("callbackUrl" , callbackUrl , ParamPosition.BODY , false);
        request.addParam("channel" , String.valueOf(channel), ParamPosition.BODY , false);



        return sendSyncRequest(request);
    }
    public void 批量获取短信下发状态(Object externalIds , ApiCallback callback) {
        String path = "/comms/sms/pullSmsSendStatusSet";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("externalIds" , String.valueOf(externalIds) , ParamPosition.BODY , true);



        sendAsyncRequest(request , callback);
    }

    public ApiResponse 批量获取短信下发状态SyncMode(Object externalIds) {
        String path = "/comms/sms/pullSmsSendStatusSet";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("externalIds" , String.valueOf(externalIds) , ParamPosition.BODY , true);



        return sendSyncRequest(request);
    }
    public void 获取单个号码短信下发状态(String externalId , ApiCallback callback) {
        String path = "/comms/sms/pullSmsSendStatus";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("externalId" , externalId , ParamPosition.BODY , false);



        sendAsyncRequest(request , callback);
    }

    public ApiResponse 获取单个号码短信下发状态SyncMode(String externalId) {
        String path = "/comms/sms/pullSmsSendStatus";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("externalId" , externalId , ParamPosition.BODY , false);



        return sendSyncRequest(request);
    }

}