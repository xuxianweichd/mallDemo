//
//  Created by  fred on 2016/10/26.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.mublo.mublomall.thirdparty.Component.sdk.cn_shanghai;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
public class Demo短信发送 {
    private String APPKEY;
    private String APPSECRET;
    private int num = 0;

    @Value("${APPKEY}")
    public void setAPPKEY(String APPKEY) {
        this.APPKEY = APPKEY;
        num++;
        if (num == 2) {
            setApp();
            num = 0;
        }
    }

    @Value("${APPSECRET}")
    public void setAPPSECRET(String APPSECRET) {
        this.APPSECRET = APPSECRET;
        num++;
        if (num == 2) {
            setApp();
            num = 0;
        }
    }

    //    static{
//        //HTTP Client init
//        HttpClientBuilderParams httpParam = new HttpClientBuilderParams();
//        System.out.println("APPKEY---------:"+APPKEY);
//        System.out.println("APPSECRET---------:"+APPSECRET);
//        httpParam.setAppKey(APPKEY);
//        httpParam.setAppSecret(APPSECRET);
//        HttpApiClient短信发送.getInstance().init(httpParam);
//        /**
//         * HTTPS Client init
//         * https请求
//         */
////        HttpClientBuilderParams httpsParam = new HttpClientBuilderParams();
////        httpsParam.setAppKey(APPKEY);
////        httpsParam.setAppSecret(APPSECRET);
//
//        /**
//         * HTTPS request use DO_NOT_VERIFY mode only for demo
//         * Suggest verify for security1
//         * 忽略证书代码
//         */
//        //httpsParam.setRegistry(getNoVerifyRegistry());
//
////        HttpsApiClient短信发送.getInstance().init(httpsParam);
//    }
    public void setApp() {
        HttpClientBuilderParams httpParam = new HttpClientBuilderParams();
        httpParam.setAppKey(APPKEY);
        httpParam.setAppSecret(APPSECRET);
        HttpApiClient短信发送.getInstance().init(httpParam);
    }

    public static void 发送短信HttpTest(String mobile, String templateID, String templateParamSet, String callbackUrl) {
        HttpApiClient短信发送.getInstance().发送短信(mobile, templateID, templateParamSet, callbackUrl, 0, new ApiCallback() {
            @Override
            public void onFailure(ApiRequest request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(ApiRequest request, ApiResponse response) {
                try {
                    System.out.println(getResultString(response));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void 发送短信HttpSyncTest() {
        ApiResponse response = HttpApiClient短信发送.getInstance().发送短信SyncMode("default", "default", "default", "default", 0);
        try {
            System.out.println(getResultString(response));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void 发送短信HttpsTest() {
        HttpsApiClient短信发送.getInstance().发送短信("default", "default", "default", "default", 0, new ApiCallback() {
            @Override
            public void onFailure(ApiRequest request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(ApiRequest request, ApiResponse response) {
                try {
                    System.out.println(getResultString(response));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void 发送短信HttpsSyncTest() {
        ApiResponse response = HttpsApiClient短信发送.getInstance().发送短信SyncMode("default", "default", "default", "default", 0);
        try {
            System.out.println(getResultString(response));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static String getResultString(ApiResponse response) throws IOException {
        StringBuilder result = new StringBuilder();
        result.append("Response from backend server").append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        result.append("ResultCode:").append(SdkConstant.CLOUDAPI_LF).append(response.getCode()).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        if (response.getCode() != 200) {
            result.append("Error description:").append(response.getHeaders().get("X-Ca-Error-Message")).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        }

        result.append("ResultBody:").append(SdkConstant.CLOUDAPI_LF).append(new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING));

        return result.toString();
    }

    private static Registry<ConnectionSocketFactory> getNoVerifyRegistry() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        try {
            registryBuilder.register("http", PlainConnectionSocketFactory.INSTANCE).build();
            registryBuilder.register(
                    "https",
                    new SSLConnectionSocketFactory(new SSLContextBuilder().loadTrustMaterial(
                            KeyStore.getInstance(KeyStore.getDefaultType()), new TrustStrategy() {
                                @Override
                                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                                    return true;
                                }
                            }).build(),
                            new HostnameVerifier() {
                                @Override
                                public boolean verify(String paramString, SSLSession paramSSLSession) {
                                    return true;
                                }
                            }));

        } catch (Exception e) {
            throw new RuntimeException("HttpClientUtil init failure !", e);
        }
        return registryBuilder.build();
    }


    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }

    static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }

        public void checkClientTrusted(
                X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
    }
}
