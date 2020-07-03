package com.mublo.mublomall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.Query;
import com.mublo.mublomall.product.dao.CategoryDao;
import com.mublo.mublomall.product.entity.CategoryBrandRelationEntity;
import com.mublo.mublomall.product.entity.CategoryEntity;
import com.mublo.mublomall.product.service.CategoryBrandRelationService;
import com.mublo.mublomall.product.service.CategoryService;
import com.mublo.mublomall.product.vo.Catelog2Vo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    private final CategoryBrandRelationService categoryBrandRelationService;
    private StringRedisTemplate redisTemplate;

    @Autowired
    public CategoryServiceImpl(CategoryBrandRelationService categoryBrandRelationService, StringRedisTemplate redisTemplate) {
        this.categoryBrandRelationService = categoryBrandRelationService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    //    @Override
//    public List<CategoryEntity> listWithTree() {
//        QueryWrapper<CategoryEntity> categoryEntityQueryWrapper=new QueryWrapper<>();
//        categoryEntityQueryWrapper.eq("parent_cid",0);
//        List<CategoryEntity> categoryEntities = baseMapper.selectList(categoryEntityQueryWrapper);
//        return categoryEntities;
//    }
    @Override
    public List<CategoryEntity> listWithTree() {

        //1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //2、组装成父子的树形结构
        //2.1）、找到所有的一级分类
        return getChildrens(0L, entities);

//        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
//                categoryEntity.getParentCid().equals(0L)
//        ).map((menu)->{
//            menu.setChildren(getChildrens(menu.getCatId(),entities));
//            return menu;
//        }).sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort()))).collect(Collectors.toList());
////        return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
//        return menu1.getSort()-menu2.getSort();
//        return level1Menus;
    }

    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(Long catId, List<CategoryEntity> all) {

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(catId);
        }).map(categoryEntity -> {
            //1、找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity.getCatId(), all));
            return categoryEntity;
        }).sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort()))).collect(Collectors.toList());
        return children;
    }

    @Override
    public List<CategoryEntity> getCategorysLevelOne() {
        //TODO Redis锁缓存的方式
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if (lock) {
            List<CategoryEntity> categoryEntitie;
            try {
                categoryEntitie = getCategoryEntities();
            } finally {
                //确保一定会释放锁
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                redisTemplate.execute(new DefaultRedisScript(script, Long.class), Arrays.asList("lock"), uuid);
                log.trace("释放锁成功");
            }
            return categoryEntitie;
        } else {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.trace("获取失败重新获取");
            return getCategorysLevelOne();
        }
    }

    public List<CategoryEntity> getCategoryEntities() {
        String catelogLevelOne = redisTemplate.opsForValue().get("catelogLevelOne");
        if (StringUtils.isNotEmpty(catelogLevelOne)) {
            log.trace("发现缓存");
            return JSON.parseObject(catelogLevelOne, new TypeReference<List<CategoryEntity>>() {
            });
        }
        log.trace("没有缓存查询数据库");
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        String s = JSON.toJSONString(categoryEntities);
        redisTemplate.opsForValue().set("catelogLevelOne", s);
        return categoryEntities;
    }

    /**
     * 获取所有一级分类之下的数据("name", "cat_id", "parent_cid")
     *
     * @return
     */
    @Override
    public synchronized Map<String, List<Catelog2Vo>> getCatalogJson() {
        //TODO 本地锁缓存的方式
//        synchronized (this){
        String catelogLevelOne = redisTemplate.opsForValue().get("CatlogVo");
        if (StringUtils.isNotEmpty(catelogLevelOne)) {
            log.trace("发现缓存");
            return JSON.parseObject(catelogLevelOne, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
        }
        log.trace("没有缓存查询数据库");
        List<CategoryEntity> categoryEntities = this.list(new QueryWrapper<CategoryEntity>().select("name", "cat_id", "parent_cid"));
        List<CategoryEntity> parentCid = getParentCid(categoryEntities, 0L);
        Map<String, List<Catelog2Vo>> collect = parentCid.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), level1 -> {
            return getCatlogVo(level1.getCatId(), categoryEntities);
        }));
        String s = JSON.toJSONString(collect);
        redisTemplate.opsForValue().set("CatlogVo", s, 300, TimeUnit.SECONDS);
//        }
        return collect;
    }

    /**
     * @param catId 父类id
     * @param all   要遍历的数据体
     * @return
     */
    private List<Catelog2Vo> getCatlogVo(Long catId, List<CategoryEntity> all) {
        List<Catelog2Vo> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(catId);
        }).map(categoryEntity -> {
            Catelog2Vo catelog2Vo = new Catelog2Vo(categoryEntity.getCatId(), categoryEntity.getName(), categoryEntity.getParentCid(), getCatlogVo(categoryEntity.getCatId(), all));
            return catelog2Vo;
        }).collect(Collectors.toList());
        return children;
    }

    /**
     * 在selectList中找到parentId等于传入的parentCid的所有分类数据
     *
     * @param selectList
     * @param parentCid
     * @return
     */
    private List<CategoryEntity> getParentCid(List<CategoryEntity> selectList, Long parentCid) {
        List<CategoryEntity> collect = selectList.stream().filter(item -> item.getParentCid() == parentCid).collect(Collectors.toList());
        return collect;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 1.检查当前删除的菜单，是否被别的地方引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCateLogPath(Long catelogId) {
        List<Long> path = new ArrayList<>();
        CategoryEntity categoryEntity = baseMapper.selectById(catelogId);
        path.add(catelogId);
        while (categoryEntity.getParentCid() != 0) {
            categoryEntity = baseMapper.selectById(categoryEntity.getParentCid());
            path.add(categoryEntity.getCatId());
        }
        Collections.reverse(path);
        return path.toArray(new Long[path.size()]);
    }

    @Override
    public void updateDetail(CategoryEntity category) {
        CategoryEntity categoryEntity = this.getById(category.getCatId());
        if (categoryEntity != null) {
            this.updateById(category);
            if (!"".equals(category.getName()) && !categoryEntity.getName().equals(category.getName())) {
                UpdateWrapper<CategoryBrandRelationEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("catelog_id", category.getCatId()).set("catelog_name", category.getName());
                categoryBrandRelationService.update(updateWrapper);
            }
        }

    }

    /**
     * 使用Redisson分布式锁来实现多个服务共享同一缓存中的数据
     * @return
     */
//    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDbWithRedissonLock() {
//
//        RLock lock = redissonClient.getLock("CatelogJson-lock");
//        //该方法会阻塞其他线程向下执行，只有释放锁之后才会接着向下执行
//        lock.lock();
//        Map<String, List<Catelog2Vo>> catelogJsonFromDb;
//        try {
//            //从数据库中查询分类数据
//            catelogJsonFromDb = getCatelogJsonFromDb();
//        } finally {
//            lock.unlock();
//        }
//
//        return catelogJsonFromDb;
//
//    }


    /**
     * 使用分布式锁来实现多个服务共享同一缓存中的数据
     * （1）设置读写锁，失败则表明其他线程先于该线程获取到了锁，则执行自旋，成功则表明获取到了锁
     * （2）获取锁成功，查询数据库，获取分类数据
     * （3）释放锁
     * @return
     */
//    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDbWithRedisLock() {
//        String uuid= UUID.randomUUID().toString();
//        //设置redis分布式锁，成功则返回true，否则返回false，该操作是原子性的
//        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
//        if(lock==null || !lock){
//            //获取锁失败，重试
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//
//            }
//            log.warn("获取锁失败，重新获取...");
//            return getCatelogJsonFromDbWithRedisLock();
//        }else{
//            //获取锁成功
//            log.warn("获取锁成功:)");
//            Map<String, List<Catelog2Vo>> catelogJsonFromDb;
//            try {
//                //从数据库中查询分类数据
//                catelogJsonFromDb = getCatelogJsonFromDb();
//            } finally {
//                //确保一定会释放锁
//                String script="if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
//                redisTemplate.execute(new DefaultRedisScript(script,Long.class),Arrays.asList("lock"),uuid);
//                log.warn("释放锁成功:)");
//            }
//            return catelogJsonFromDb;
//        }
//
//    }


}