package com.trysixth.sixth.utils;
import org.springframework.cache.CacheManager;


public class CacheUtils {

    
    private CacheManager cacheManager;
    public CacheUtils(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }


    public void saveToCache(Object key, Object value) {
        cacheManager.getCache("SixthSpringBoot").put(key, value);
    }

    public Object getFromCache(Object key) {
        return cacheManager.getCache("SixthSpringBoot").get(key, Object.class);
    }
}