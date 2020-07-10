/*
 * Copyright (c) 2020. 
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.thirdparty;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class MublomallThirdPartyApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    OSSClient ossClient;
    @Test
    public void testUpload() throws FileNotFoundException {
//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
//// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
//        String accessKeyId = "LTAI4G2Vb9kAH8YsfZKN79yZ";
//        String accessKeySecret = "qerlYHCH0qzHnLuRJmbThVJpkyKlDH";
//
//// 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 上传文件流。
        InputStream inputStream = new FileInputStream("C:\\Users\\Administrator\\Documents\\WeChat Files\\Draken66\\FileStorage\\File\\2020-05\\61c2c11ee30d4efccb0e85b60c15caef_t.gif");
        ossClient.putObject("mublomall", "de.gif", inputStream);

// 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("上传完成");
        System.out.println(ossClient.getEndpoint());
        System.out.println(ossClient.getClientConfiguration().getProxyUsername());
    }

}
