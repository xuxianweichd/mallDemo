//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.mublo.mublomall.thirdparty.Component.sdk.cn_hangzhou;

import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;


public class HttpApiClient艾科瑞特_智能图像分析_智能人脸脸型分析分类识别 extends ApacheHttpClient{
    public final static String HOST = "faceshape.market.alicloudapi.com";
    static HttpApiClient艾科瑞特_智能图像分析_智能人脸脸型分析分类识别 instance = new HttpApiClient艾科瑞特_智能图像分析_智能人脸脸型分析分类识别();
    public static HttpApiClient艾科瑞特_智能图像分析_智能人脸脸型分析分类识别 getInstance(){return instance;}

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTP);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }




    public void 智能人脸脸型分析分类识别(String IMAGE , ApiCallback callback) {
        String path = "/ai_image_single_classification/ai_face_shape/face_shape/v1";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("IMAGE" , IMAGE , ParamPosition.BODY , true);



        sendAsyncRequest(request , callback);
    }

    public ApiResponse 智能人脸脸型分析分类识别SyncMode(String IMAGE) {
        String path = "/ai_image_single_classification/ai_face_shape/face_shape/v1";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("IMAGE" , IMAGE , ParamPosition.BODY , true);



        return sendSyncRequest(request);
    }

}