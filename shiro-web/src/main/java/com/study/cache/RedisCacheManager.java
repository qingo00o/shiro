package com.study.cache;

import javax.annotation.Resource;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

public class RedisCacheManager implements CacheManager{

	@Resource
	private RedisCache redisCache;
	@Override
	public <K, V> Cache<K, V> getCache(String cacheName) throws CacheException {
		return redisCache;
	}

}
