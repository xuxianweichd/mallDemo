/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.Query;
import com.mublo.common.utils.R;
import com.mublo.common.utils.constant.WareConstant;
import com.mublo.mublomall.ware.dao.PurchaseDao;
import com.mublo.mublomall.ware.entity.PurchaseDetailEntity;
import com.mublo.mublomall.ware.entity.PurchaseEntity;
import com.mublo.mublomall.ware.entity.WareSkuEntity;
import com.mublo.mublomall.ware.feign.ProductFeignService;
import com.mublo.mublomall.ware.service.PurchaseDetailService;
import com.mublo.mublomall.ware.service.PurchaseService;
import com.mublo.mublomall.ware.service.WareSkuService;
import com.mublo.mublomall.ware.vo.MergeVo;
import com.mublo.mublomall.ware.vo.PurchaseDoneVo;
import com.mublo.mublomall.ware.vo.PurchaseItemDoneVo;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    private final PurchaseDetailService purchaseDetailService;
    private final WareSkuService wareSkuService;
    private final ProductFeignService productFeignService;

    @Autowired
    public PurchaseServiceImpl(PurchaseDetailService purchaseDetailService, WareSkuServiceImpl wareSkuService, ProductFeignService productFeignService) {
        this.purchaseDetailService = purchaseDetailService;
        this.wareSkuService = wareSkuService;
        this.productFeignService = productFeignService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> purchaseEntityQueryWrapper = new QueryWrapper<>();
        if (!"".equals(params.get("key")) &&params.get("key") != null ) {
            String key = (String) params.get("key");
            purchaseEntityQueryWrapper.and(purchaseEntityQueryWrapper1 -> {
                purchaseEntityQueryWrapper1.eq("assignee_id", key).or().like("assignee_name", key);
            });
        }
        if (!"".equals(params.get("status")) &&params.get("status") != null) {
            String status = (String) params.get("status");
            purchaseEntityQueryWrapper.eq("status", status);
        }
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                purchaseEntityQueryWrapper
        );
        final List<PurchaseEntity> purchaseEntityList = page.getRecords().stream().map(purchase -> {
//            if (StringUtils.isNullOrEmpty(purchase.getWareId())){
//
//            }
            List<PurchaseDetailEntity> purchaseDetailEntityList = purchaseDetailService.list(
                    new QueryWrapper<PurchaseDetailEntity>()
                            .eq("purchase_id", purchase.getId())
            );
//                String wareIds = purchaseDetailEntityList.stream().map(purchaseDetai -> {
//                    return purchaseDetai.getWareId().toString();
//                }).collect(Collectors.joining(","));
//                List<Long> collect = purchaseDetailEntityList.stream().map(purchaseDetai -> {
//                    return purchaseDetai.getWareId();
//                }).collect(Collectors.toList());
//                HashSet hashSet = new HashSet(collect);
            if (purchaseDetailEntityList.size()>0){
                String wareIds = purchaseDetailEntityList.stream().map(purchaseDetai -> {
                    return purchaseDetai.getWareId().toString();
                }).distinct().collect(Collectors.joining(","));
                if (!purchase.getWareId().equals(wareIds)){
                    purchase.setWareId(wareIds);
                    this.updateById(purchase);
                }
            }
//                HashSet hashSet = new HashSet(collect);
            return purchase;
        }).collect(Collectors.toList());
        page.setRecords(purchaseEntityList);
        return new PageUtils(page);
    }


    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().in("status", WareConstant.PurchaseStatusEnum.getReceive())
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        if (purchaseId == null) {
            //1、新建一个
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        //TODO 确认采购单状态是0,1才可以合并

        List<Long> items = mergeVo.getItems();
//        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
//            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
//            detailEntity.setId(i);
//            detailEntity.setPurchaseId(finalPurchaseId);
//            detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
//            return detailEntity;
//        }).collect(Collectors.toList());
//        purchaseDetailService.updateBatchById(collect);
        purchaseDetailService.update(
                new UpdateWrapper<PurchaseDetailEntity>()
                        .in("id", items)
                        .in("status", WareConstant.PurchaseDetailStatusEnum.getReceive())
                        .set("status", WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode())
                        .set("purchase_id", purchaseId)
        );
//        final Long finalPurchaseId = purchaseId;
//        List<PurchaseDetailEntity> purchaseDetailEntityList = purchaseDetailService.listByIds(items).stream()
//                .filter( item->
//                        item.getStatus() == WareConstant.PurchaseDetailStatusEnum.CREATED.getCode() ||
//                                item.getStatus() == WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode()
//                )
//                .map(item->{
//                    item.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
//                    item.setPurchaseId(finalPurchaseId);
//                    return item;
//                })
//                .collect(Collectors.toList());
//        purchaseDetailService.updateBatchById(purchaseDetailEntityList);
//        String collect = purchaseDetailEntityList.stream().map(item -> {
//            return item.getWareId().toString();
//        }).collect(Collectors.joining(","));

        purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
//        baseMapper.updateDetail(purchaseId,collect,new Date());
        this.updateById(purchaseEntity);
    }

    @Transactional
    @Override
    public void received(List<Long> ids) {
        List<PurchaseDetailEntity> list = purchaseDetailService.list(
                new QueryWrapper<PurchaseDetailEntity>()
                        .in("purchase_id", ids)
                        .eq("status", WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode())
        );
        if (list.size()==0){
            throw new RuntimeException("该采购单还未有采购需求");
        }
        purchaseDetailService.update(
                new UpdateWrapper<PurchaseDetailEntity>()
                        .in("purchase_id", ids)
                        .eq("status", WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode())
                        .set("status", WareConstant.PurchaseDetailStatusEnum.BUYING.getCode())
        );
        this.update(
                new UpdateWrapper<PurchaseEntity>()
                .in("id", ids)
                .in("status", WareConstant.PurchaseStatusEnum.getReceive())
                .set("status", WareConstant.PurchaseStatusEnum.RECEIVE.getCode())
        );
        
        
    }

    @Transactional
    @Override
    public void done(PurchaseDoneVo purchaseDoneVo) {
        //获取当前采购单下的所有采购需求信息
        List<PurchaseItemDoneVo> items = purchaseDoneVo.getItems();
        //判断是否存在有某些原因导致失败的采购需求
        Boolean judge = true;
        List<Long> success = new ArrayList<>();
        List<Long> fail = new ArrayList<>();
        //把所有成功采购的需求和失败的需求分别存在不同的集合
        for (PurchaseItemDoneVo purchaseItemDoneVo : items) {
            if (purchaseItemDoneVo.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                judge = false;
                fail.add(purchaseItemDoneVo.getItemId());
            } else {
                success.add(purchaseItemDoneVo.getItemId());
            }
        }
        if (success.size() > 0) {
            purchaseDetailService.update(new UpdateWrapper<PurchaseDetailEntity>().in("id", success).set("status", WareConstant.PurchaseDetailStatusEnum.FINISH.getCode()));
            List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.listByIds(success);
            List<WareSkuEntity> list = new ArrayList<>();
            for (int i = 0; i < success.size(); i++) {
                WareSkuEntity wareSkuEntity = wareSkuService.getOne(new QueryWrapper<WareSkuEntity>().eq("sku_id", success.get(i)).eq("ware_id", purchaseDetailEntities.get(i).getWareId()));
                if (wareSkuEntity == null) {
                    wareSkuEntity = new WareSkuEntity();
                    wareSkuEntity.setSkuId(success.get(i));
                    wareSkuEntity.setStock(purchaseDetailEntities.get(i).getSkuNum());
                    wareSkuEntity.setWareId(purchaseDetailEntities.get(i).getWareId());
                    wareSkuEntity.setStockLocked(0);
                    //TODO 远程查询sku的名字，如果失败，整个事务无需回滚
                    //1、自己catch异常
                    //TODO 还可以用什么办法让异常出现以后不回滚？高级
                    try {
                        R info = productFeignService.info(success.get(i));
                        Map<String, Object> data = (Map<String, Object>) info.get("skuInfo");
                        if (info.getCode() == 0) {
                            wareSkuEntity.setSkuName((String) data.get("skuName"));
                        }
                    } catch (Exception e) {

                    }
                }else {
                    wareSkuEntity.setStock(wareSkuEntity.getStock()+purchaseDetailEntities.get(i).getSkuNum());
                }
                list.add(wareSkuEntity);
            }
            wareSkuService.saveOrUpdateBatch(list);
        }
        if (fail.size() > 0) {
            purchaseDetailService.update(new UpdateWrapper<PurchaseDetailEntity>().in("id", fail).set("status", WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()));
        }
        //获取当前采购单下所有成功的采购需求
        List<PurchaseDetailEntity> purchaseDetailEntityList = purchaseDetailService.list(
                new QueryWrapper<PurchaseDetailEntity>()
                        .eq("purchase_id", purchaseDoneVo.getId())
                        .eq("status",WareConstant.PurchaseDetailStatusEnum.FINISH)
        );

        //1、改变采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseDoneVo.getId());
        purchaseEntity.setStatus(judge ?
                purchaseDetailEntityList.size()==success.size()?WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.RECEIVE.getCode() :
                WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

}