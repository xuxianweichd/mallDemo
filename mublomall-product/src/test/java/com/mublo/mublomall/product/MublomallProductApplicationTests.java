package com.mublo.mublomall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ImmutableList;
import com.mublo.mublomall.product.entity.BrandEntity;
import com.mublo.mublomall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@SpringBootTest
class MublomallProductApplicationTests {
    @Autowired
    BrandService brandService;
//    @Test
//    public void testUpload() {
//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
//        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
//        String accessKeyId = "<yourAccessKeyId>";
//        String accessKeySecret = "<yourAccessKeySecret>";
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//        // 创建PutObjectRequest对象。
//        PutObjectRequest putObjectRequest = new PutObjectRequest("<yourBucketName>", "<yourObjectName>", new File("<yourLocalFile>"));
//
//        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
//        // ObjectMetadata metadata = new ObjectMetadata();
//        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
//        // metadata.setObjectAcl(CannedAccessControlList.Private);
//        // putObjectRequest.setMetadata(metadata);
//
//        // 上传文件。
//        ossClient.putObject(putObjectRequest);
//
//        // 关闭OSSClient。
//        ossClient.shutdown();
//    }
    @Test
    void contextLoads() {
//        BrandEntity brandEntity=new BrandEntity();
//        brandEntity.setName("mublo");
//        brandEntity.setDescript("Test");
//        brandService.save(brandEntity);
//        System.out.println("保存成功");
//        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("name", "mublo"));
//        list.forEach(item -> {
//            System.out.println(item);
//        });

        List<String> list = ImmutableList.of("Hollis","hollischuang","Java干货");
        list.stream().collect(Collectors.joining(":"));
        System.out.println(list.toString());
//        CollectorImpl CollectorImpl=new CollectorImpl();
        System.out.println(Collectors.joining(":").toString());
//        StringJoiner sj1 = new StringJoiner(":","[","]");
//        sj1.add("Hollis").add("hollischuang").add("Java干货");
//        sj1.toString();

//        String.join(",",list);
    }

}
