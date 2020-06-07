package com.mublo.mublomall.ware.service.impl;

import com.mublo.common.utils.constant.WareConstant;
import com.mublo.mublomall.ware.entity.PurchaseDetailEntity;
import com.mublo.mublomall.ware.service.PurchaseDetailService;
import com.mublo.mublomall.ware.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.Query;

import com.mublo.mublomall.ware.dao.PurchaseDao;
import com.mublo.mublomall.ware.entity.PurchaseEntity;
import com.mublo.mublomall.ware.service.PurchaseService;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    private final PurchaseDetailService purchaseDetailService;

    @Autowired
    public PurchaseServiceImpl(PurchaseDetailService purchaseDetailService) {
        this.purchaseDetailService = purchaseDetailService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> purchaseEntityQueryWrapper = new QueryWrapper<>();
        if (params.get("key") != null) {
            String key = (String) params.get("key");
            purchaseEntityQueryWrapper.and(purchaseEntityQueryWrapper1 -> {
                purchaseEntityQueryWrapper1.eq("assignee_id", key).or().like("assignee_name", key);
            });
        }
        if (params.get("status") != null) {
            String status = (String) params.get("status");
            purchaseEntityQueryWrapper.eq("priority", status);
        }
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                purchaseEntityQueryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().in("status", 0, 1)
        );

        return new PageUtils(page);
    }

    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null) {
            //1、新建一个
            PurchaseEntity purchaseEntity = new PurchaseEntity();

            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();

            detailEntity.setId(i);
            detailEntity.setPurchaseId(finalPurchaseId);
            detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return detailEntity;
        }).collect(Collectors.toList());


        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

}