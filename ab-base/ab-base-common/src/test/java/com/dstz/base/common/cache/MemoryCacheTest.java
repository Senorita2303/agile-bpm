package com.dstz.base.common.cache;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

public class MemoryCacheTest {

    @Test
    public void test_getIfPresent() throws Exception {
        MemoryCache memoryCache = new MemoryCache(Arrays.asList(
                new CacheRegion("test1", Duration.ofSeconds(1L)),
                new CacheRegion("test2", Duration.ofSeconds(1L))
        ));
        // 放入缓存数据
        memoryCache.put("test1", "name", "张三");
        memoryCache.put("test2", "name", "李四");

        // 断言缓存内数据
        Assert.assertEquals("张三", memoryCache.getIfPresent("test1", "name"));
        Assert.assertEquals("李四", memoryCache.getIfPresent("test2", "name"));

        // 断言过期数据
        Thread.sleep(1000L);
        Assert.assertNull(memoryCache.getIfPresent("test1", "name"));
        Assert.assertNull(memoryCache.getIfPresent("test2", "name"));

        // 测试未定义区域
        Assert.assertThrows("test3 is undefined", IllegalArgumentException.class, () -> {
            memoryCache.getIfPresent("test3", "name");
        });
    }

    @Test
    public void test_get() throws Exception {
        MemoryCache memoryCache = new MemoryCache(Collections.singletonList(new CacheRegion("test", Duration.ofSeconds(1L))));
        // 测试数据不存在加载
        Assert.assertEquals("张三", memoryCache.get("test", "name", () -> "张三"));
        Assert.assertEquals("张三", memoryCache.getIfPresent("test", "name"));

        Thread.sleep(1000L);
        Assert.assertNull(memoryCache.getIfPresent("test", "name"));
    }

    @Test
    public void test_invalidate() throws Exception {
        MemoryCache memoryCache = new MemoryCache(Arrays.asList(
                new CacheRegion("test1", Duration.ofSeconds(10L)),
                new CacheRegion("test2", Duration.ofSeconds(10L))
        ));

        memoryCache.put("test1", "name", "张三");
        memoryCache.put("test2", "name", "李四");

        // 清理test1区域缓存
        memoryCache.invalidate("test1", "name");

        Assert.assertEquals("李四", memoryCache.getIfPresent("test2", "name"));

        memoryCache.invalidateAll();
        Assert.assertNull(memoryCache.getIfPresent("test1", "name"));
        Assert.assertNull(memoryCache.getIfPresent("test2", "name"));
    }

    @Test
    public void test_exists() throws Exception {
        MemoryCache memoryCache = new MemoryCache(Arrays.asList(
                new CacheRegion("test1", Duration.ofSeconds(10L)),
                new CacheRegion("test2", Duration.ofSeconds(10L))
        ));
        Assert.assertFalse(memoryCache.exists("test1", "name"));
        memoryCache.put("test1", "name", "张三");
        Assert.assertTrue(memoryCache.exists("test1", "name"));
    }

}