package com.mublo.mublomall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ImmutableList;
import com.mublo.common.utils.BeanCopierUtil;
import com.mublo.common.utils.to.SpuBoundTo;
import com.mublo.mublomall.product.entity.BrandEntity;
import com.mublo.mublomall.product.service.BrandService;
import com.mublo.mublomall.product.vo.Bounds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.math.BigDecimal;
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
        long start = System.currentTimeMillis();
        System.out.println("开始时间"+start);
//        List<String> list = ImmutableList.of("Hollis","hollischuang","Java干货");
//        List<String> list = new ArrayList<>();
//        for (int i=0;i<500000;i++){
//            list.add(String.valueOf(i));
//        }
//        StringBuilder stringBuilder=new StringBuilder(288888);
//        List<String> lis=list.stream().map((item)->{
//            return stringBuilder.append(item+",").toString();
//        }).collect(Collectors.toList());
//            String val=list.stream().reduce((a,b)->{
//                return stringBuilder.append(b+",").toString();
//            }).get();
//        System.out.println(val);
//        System.out.println(val.length());
//        System.out.println(list.stream().collect(Collectors.joining(":")).toString());

//        System.out.println(list.toString());
//        CollectorImpl CollectorImpl=new CollectorImpl();
//        System.out.println(Collectors.joining(":").toString());
//        StringJoiner sj1 = new StringJoiner(":","[","]");
//        sj1.add("Hollis").add("hollischuang").add("Java干货");
//        sj1.toString();

//        System.out.println(String.zjoin(",",list).toString());;
//        List<Bounds> list=new ArrayList<>();
//        List<SpuBoundTo> spuBoundToList=new ArrayList<>();
//        for (int i=0;i<1000000;i++){
//            Bounds bounds=new Bounds();
//            bounds.setBuyBounds(new BigDecimal(i));
//            bounds.setGrowBounds(new BigDecimal(i));
//            list.add(bounds);
//            spuBoundToList.add(new SpuBoundTo());
//        }
//
////        SpuBoundTo spuBoundTo=new SpuBoundTo();
//
////        for (Bounds bounds:list){
//////            for (SpuBoundTo spuBoundTo:spuBoundToList){
//////                System.out.print(bounds.getBuyBounds());
//////                BeanCopierUtil.copy(bounds,spuBoundTo);
//////            }
////            BeanCopierUtil.copy(bounds,spuBoundTo);
////        }
//        for (int i=0;i<list.size();i++){
//            BeanUtils.copyProperties(list.get(i),spuBoundToList.get(i));
////            BeanCopierUtil.copy(list.get(i),spuBoundToList.get(i));
////            BeanCopier beanCopier=BeanCopier.create(list.get(0).getClass(),spuBoundToList.get(0).getClass(),false);
////            beanCopier.copy(list.get(i),spuBoundToList.get(i),null);
////            System.out.print(spuBoundToList.get(i).getBuyBounds());
//        }
//        Bounds bounds=new Bounds();
//        bounds.setGrowBounds(BigDecimal.ONE);
//        final Object[] test={1,bounds};
//        Object[] test2={2,3};
//        test=test2;
//        System.out.println((Integer) test[0]);
//        test[0]=2;
//        System.out.println((Integer) test[0]);
        String one="{'itemId':";
        String two=",'status':3,'reason':''},";
        String all="";
        for (int i=7;i<33;i++){
            all+=one+i+two;
        }
        System.out.println(all);
        long end = System.currentTimeMillis();
        System.out.println("结束时间"+end);
        System.out.println("用时"+(end-start));
    }

}
