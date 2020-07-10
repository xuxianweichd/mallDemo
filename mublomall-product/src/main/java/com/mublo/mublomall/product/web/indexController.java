/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.product.web;

import com.mublo.mublomall.product.entity.CategoryEntity;
import com.mublo.mublomall.product.service.CategoryService;
import com.mublo.mublomall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;
@Slf4j
@Controller
public class indexController {
    private final CategoryService categoryService;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate redisTemplate;

    public indexController(CategoryService categoryService, RedissonClient redissonClient, StringRedisTemplate redisTemplate) {
        this.categoryService = categoryService;
        this.redissonClient = redissonClient;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping({"/","index"})
    public String indexPage(Model model){
        List<CategoryEntity> categoryEntities=categoryService.getCategorysLevelOne();
        model.addAttribute("categorys", categoryEntities);
//        getCataLog();
        return "index";
    }
    @GetMapping("index/catalog.json")
    @ResponseBody
    public Map<String, List<Catelog2Vo>> getCataLog() {
        Map<String, List<Catelog2Vo>> json = categoryService.getCatalogJson();
        return json;
    }

    /**
     * 读写锁的运用方式
     * 保证一定能读到最新数据，修改期间，写锁是一个排他锁（互斥锁，独享锁）。读锁屎一个共享锁
     * 写锁没释放读就必须等待
     * 读 + 读 ：相当于无锁，并发读，只会在redis中记录好，所有当前的读锁。他们都会同时加锁成功
     * 写 + 读 ：等待写锁释放
     * 写 + 写 ：等待上一个写锁释放
     * 读 + 写 ：写需要等待读锁释放
     * @return
     */
    @GetMapping("/write")
    @ResponseBody
    public String writeValue(){
        RReadWriteLock writeLock=redissonClient.getReadWriteLock("rw-loc");
        String uuid = null;
        RLock lock = writeLock.writeLock();
        lock.lock();
        try {
            log.info("写锁加锁成功");
            uuid = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set("writeValue",uuid);
            Thread.sleep(30000);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
            log.info("写锁释放");

        }
        return uuid;
    }

    /**
     * 读锁
     * @return
     */
    @GetMapping("/read")
    @ResponseBody
    public String redValue(){
        String uuid = null;
        RReadWriteLock readLock=redissonClient.getReadWriteLock("rw-loc");
        RLock lock = readLock.readLock();
        lock.lock();
        try {
            log.info("读锁加锁成功");
            uuid = redisTemplate.opsForValue().get("writeValue");
            Thread.sleep(30000);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
            log.info("读锁释放");
        }
        return uuid;
    }
    /**
     * 信号量的运用方式
     * 车位占位
     * 限流
     */
    @GetMapping("/park")
    @ResponseBody
    public String park(){
        RSemaphore park=redissonClient.getSemaphore("pack");
//        park.tryAcquire()获取一个信号，减少一个车位
        boolean b=park.tryAcquire();
        return "ok=>"+b;
    }
    /**
     * 车位让位
     */
    @GetMapping("/go")
    @ResponseBody
    public void go(){
        RSemaphore park=redissonClient.getSemaphore("pack");
//        park.release()释放一个信号，增加一个车位
        park.release();
    }
    /**
     * 闭锁的运用方式
     * 放假锁门
     * 指定班级数量走完才能锁门
     */
    @GetMapping("/lockDoor")
    @ResponseBody
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        //指定需要释放的锁数量
        door.trySetCount(5);
        door.await();//等待全部释放完成
        return "放假了";
    }
    /**
     * 车位让位
     */
    @GetMapping("/gogogo/{id}")
    @ResponseBody
    public String go(@PathVariable int id){
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown();//释放一个
        return id+"班的人都走了。。。。";
    }
    /**
     * 每一个需要缓存的数据都指定要放哪个名字的缓存。【缓存的分区（按照业务类型分）】
     * @Cacheable 代表当前方法的结果需要缓存，如果缓存种有，方法不调用。如果缓存种没有，会调用方法
     */
    @Cacheable({"TestCache"})
    @GetMapping("/TestCache")
    @ResponseBody
    public String TestCache(){
        System.out.println("TestCache");
        return "TestCache。。。。";
    }
}
