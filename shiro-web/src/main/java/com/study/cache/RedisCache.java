package com.study.cache;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.study.util.JedisUtil;
/**
 * 
 * @Description 缓存管理
 * @author DuJian
 * @Date 2018年9月28日下午1:50:33
 * @param <K>
 * @param <V>
 */
@Component
public class RedisCache<K, V>  implements Cache<K, V>{
	
	@Resource
	private JedisUtil jedisUtil;
	
	private final String CACHE_PREFIX ="shiro-study:";
	
	
	private byte[] getKey(K k) {
		System.out.println("从缓存中获取数据");
		if (k instanceof String) {
			return (CACHE_PREFIX+k).getBytes();
		}
		
		return SerializationUtils.serialize(k);
	}

	@Override
	public void clear() throws CacheException {
		
	}

	@Override
	public V get(K k) throws CacheException {
		byte[] value = jedisUtil.get(getKey(k));
		if (value !=null) {
			return (V) SerializationUtils.deserialize(value);
		}
		return null;
	}

	@Override
	public Set<K> keys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V put(K k, V v) throws CacheException {
		byte[] key = getKey(k);
		byte[] value = SerializationUtils.serialize(v);
		
		jedisUtil.set(key, value);
		jedisUtil.expire(key, 600);
		return null;
	}

	@Override
	public V remove(K k) throws CacheException {
		byte[] key = getKey(k);
		byte[] value = jedisUtil.get(key);
		jedisUtil.del(key);
		if (value!=null) {
			return (V) SerializationUtils.deserialize(value);
		}
		 
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

}
